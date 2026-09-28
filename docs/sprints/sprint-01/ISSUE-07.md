# Sprint 1 — Issue 7: Inyección global con Hilt

## Ficha

- Rol responsable: Dev Mobile
- Rama: `feature/sprint-1-issue-7-global-hilt`
- Rama base temporal: `feature/sprint-1-issue-5-design-system`
- Prioridad: P0
- Estado: Implementado y validado localmente; pendiente de PR

## Entregables

- Abstracción inyectable `CoroutineDispatchers` con `main`, `io` y `default`.
- `DispatchersModule` y `UiModule` instalados en `SingletonComponent`.
- `AudioHapticHelper` singleton con `@ApplicationContext`.
- `BaseViewModel` con constructor inyectado, `StateFlow` inmutable y coroutine
  ejecutada mediante un dispatcher sustituible.
- Resolución del ViewModel por `hiltViewModel()` dentro de Navigation Compose.
- Prueba unitaria con `kotlinx-coroutines-test`.
- Verificación automática de los límites entre Core, UI, Domain y Data.

## Puntos de entrada

- `AsistenciaApp` conserva `@HiltAndroidApp` y su registro en el manifest.
- `MainActivity` conserva `@AndroidEntryPoint`.
- `AppNavHost` crea el ViewModel con el scope de su destino de navegación.

## Dependencia entre ramas

El Issue 7 necesita los entregables de los Issues 3, 4 y 5. Como todavía no
están integrados en `develop`, esta rama está apilada temporalmente sobre la del
Issue 5. El Issue 6 no forma parte de este cambio. Tras integrar las ramas base,
debe rebasarse sobre `develop` antes del PR definitivo.

## Verificación

```text
./gradlew verifyDomainPurity verifyLayerBoundaries
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

Validación local del 21 de agosto de 2026:

- `verifyDomainPurity`: aprobado.
- `verifyLayerBoundaries`: aprobado.
- `testDebugUnitTest`: 5 pruebas, 0 fallos, 0 errores, 0 omitidas.
- `assembleDebug`: aprobado.
- Grafo Hilt/KSP: generado y compilado correctamente.
- APK: `app/build/outputs/apk/debug/app-debug.apk` (11 361 431 bytes).
- SHA-256: `194E1D9968C384F86818C2DA66721800DD097767805223DF424AA5086FC31DCA`.
- Resultado: `BUILD SUCCESSFUL`, 53 tareas verificadas.
