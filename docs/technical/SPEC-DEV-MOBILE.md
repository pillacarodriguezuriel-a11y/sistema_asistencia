# Especificación de implementación — Dev Mobile

## Alcance

Dev Mobile implementará los flujos Compose/MVVM de importación, escaneo y
exportación, además del ciclo de vida CameraX, permisos, feedback y Share
Intent. Consumirá resultados tipados de dominio; la UI no leerá Excel ni
consultará Room directamente.

## Arquitectura y límites

- Flujo unidireccional: `UiEvent -> ViewModel -> UseCase -> UiState/UiEffect`.
- El estado durable vive en `StateFlow`; navegación, vibración, sonido y apertura
  del chooser son efectos de una sola emisión (`SharedFlow` o equivalente).
- ViewModel no recibe `Activity`, `Context` ni objetos CameraX.
- Composables no contienen reglas de duplicado, parsing de DNI ni transacciones.
- Las operaciones largas se cancelan con el ciclo de vida y nunca bloquean Main.

## Flujo HU-01: importación

1. El usuario elige `.xlsx` o `.csv` mediante Storage Access Framework con
   `ACTION_OPEN_DOCUMENT`; no se solicita permiso general de almacenamiento.
2. La pantalla conserva permiso de lectura del URI cuando sea necesario.
3. El ViewModel emite `Importing` y deshabilita una segunda confirmación.
4. El caso de uso devuelve `Success`, `InvalidHeaders`, `InvalidRows`,
   `IdentityConflict` o `Failure`.
5. La UI muestra resumen de insertados/actualizados o errores accionables con
   fila y causa. Nunca afirma éxito si ocurrió rollback.

Estados mínimos: `Idle`, `FileSelected`, `Importing`, `Imported`, `Rejected` y
`Error`.

## Flujo HU-02: escaneo

### CameraX

- Solicitar `CAMERA` en tiempo de ejecución y ofrecer captura manual si se
  deniega.
- Vincular `Preview` e `ImageAnalysis` al `LifecycleOwner` visible.
- Usar cámara trasera y `STRATEGY_KEEP_ONLY_LATEST`.
- Analizar en executor dedicado y cerrar cada `ImageProxy` en `finally`.
- Configurar el lector solo para `PDF_417` y `QR_CODE` y en modalidad offline.
- Pausar el análisis mientras un resultado está en proceso y reanudarlo después
  de mostrar feedback; liberar casos de uso al abandonar la pantalla.
- No registrar CameraX repetidamente en cada recomposición; encapsular y limpiar
  recursos con APIs de efecto/ciclo de vida apropiadas.

### Estado y efectos

```kotlin
data class AttendanceUiState(
    val activityId: Long,
    val cameraPermission: CameraPermissionState,
    val scanningEnabled: Boolean,
    val flashEnabled: Boolean,
    val attendanceCount: Int,
    val lastResult: ScanPresentation?,
    val manualEntryVisible: Boolean,
)

sealed interface AttendanceEffect {
    data object SuccessFeedback : AttendanceEffect
    data object DuplicateFeedback : AttendanceEffect
    data object WarningFeedback : AttendanceEffect
    data class ShowMessage(val text: String) : AttendanceEffect
}
```

Mapeo obligatorio:

| Resultado de dominio | UI | Efecto |
|---|---|---|
| `Registered` | nombre y conteo actualizado | vibración corta + pitido |
| `AlreadyRegistered` | `El estudiante ya fue registrado` | doble vibración + alerta |
| `StudentNotFound` | acciones manual/invitado | sonido de advertencia |
| trama ambigua/no válida | mensaje reintentable | advertencia, sin escritura |

El feedback debe respetar las preferencias de sonido/vibración y no debe
repetirse por recomposición.

## Flujo HU-03: exportación y compartir

1. El botón emite un evento único y queda deshabilitado mientras se exporta.
2. El ViewModel expone progreso y solicita al exporter un archivo/URI.
3. Ante éxito, la capa Android abre `Intent.ACTION_SEND` mediante chooser con:
   - MIME XLSX oficial;
   - URI `content://` de `FileProvider`;
   - `Intent.EXTRA_STREAM`;
   - `FLAG_GRANT_READ_URI_PERMISSION` y `ClipData` cuando corresponda.
4. Ante fallo, no abre el chooser y ofrece reintentar.
5. La aplicación permite además guardar mediante `ACTION_CREATE_DOCUMENT` si el
   diseño del Issue correspondiente incorpora la acción `Guardar`.

Estados mínimos: `Idle`, `Exporting`, `ReadyToShare` y `ExportError`.

## Accesibilidad y operación en campo

- Ningún estado depende solo de color, vibración o sonido; siempre hay texto o
  icono con descripción accesible.
- Los controles de flash, entrada manual y cierre tienen etiquetas para lector
  de pantalla y objetivos táctiles adecuados.
- La pantalla mantiene respuesta durante importaciones y exportaciones.
- Los mensajes no muestran DNI completo salvo que el diseño y la validación de
  privacidad lo requieran explícitamente.
- Todo el flujo debe demostrarse en modo avión.

## Pruebas mínimas del Issue Dev Mobile

- Reductor/ViewModel para todos los resultados tipados sin Android real.
- El feedback de éxito/duplicado/error se emite una sola vez.
- Permiso concedido, denegado y denegado permanentemente.
- `ImageProxy.close()` incluso si el analizador falla.
- Salida y reentrada de la pantalla sin cámara duplicada ni fuga.
- Selección de archivo, estado de progreso y error de importación.
- Intent de compartir con MIME, URI y permisos correctos.
- Flujo completo de importación, escaneo y exportación en modo avión.

## Fuera de alcance

Parsing de archivos en la UI, acceso directo a DAO, reglas de integridad en el
ViewModel, URI `file://`, permisos amplios de almacenamiento o dependencia de
servicios online.
