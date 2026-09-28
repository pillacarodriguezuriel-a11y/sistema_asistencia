# Sprint 2 — Issue 15: Pruebas de persistencia Room In-Memory

## Ficha

- Rol responsable: QA
- Estado: Implementado y aprobado localmente; pendiente de PR
- Prioridad: P0

## Estrategia

La suite JVM usa Robolectric con API 35 y una `AppDatabase` real en memoria.
Cada prueba crea una base aislada, habilita consultas en el hilo de prueba y la
cierra al finalizar.

## Matriz de pruebas

| Riesgo | Evidencia automatizada |
| --- | --- |
| Código duplicado | La segunda inserción falla y permanece una fila |
| DNI duplicado | El índice único rechaza el conflicto |
| Importación repetida | `insertAll(REPLACE)` resuelve código o DNI sin duplicar |
| Búsqueda | Nombre, código, DNI y correo devuelven la fila exacta |
| Estudiante inexistente | La clave foránea rechaza la asistencia |
| Doble marcado | El índice `actividadId + estudianteCodigo` lo impide |
| Eliminación de actividad | Las asistencias se eliminan por `CASCADE` |

La suite instrumentada previa continúa compilándose para validación en SQLite
de dispositivo, incluida la prueba de latencia P95. Su ejecución física sigue
siendo un requisito antes del merge cuando haya emulador o dispositivo.

## Resultado local

- `testDebugUnitTest`: 17 pruebas, 0 fallos, 0 errores, 0 omitidas.
- `assembleDebugAndroidTest`: APK de pruebas generada correctamente.
