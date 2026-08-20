# Sprint 1 — Issue 3: Base Android con Compose y Hilt

## Ficha

- Rol responsable: Dev Mobile
- Rama: `feature/sprint-1-issue-3-compose-hilt`
- Prioridad: P0
- Estado: Implementado y validado localmente; pendiente de CI/PR

## Entregables

- Plugins Kotlin, Compose Compiler, KSP y Hilt en Gradle Kotlin DSL.
- Compose Material 3, Navigation, Lifecycle, Coroutines y Hilt Navigation.
- `AsistenciaApp` registrada mediante `@HiltAndroidApp`.
- `MainActivity` con `@AndroidEntryPoint`, edge-to-edge, tema y NavHost.
- Paquetes base de Clean Architecture para `ui`, `domain`, `data` y `di`.
- Tema claro/oscuro con paleta semántica del contexto UI/UX.
- Módulo Hilt instalado en `SingletonComponent`.

## Decisiones técnicas

### KSP en lugar de kapt

Hilt procesa anotaciones mediante KSP. Se evita mantener dos pipelines de
procesamiento y se conserva la configuración compatible con Kotlin 2.1.21.

### Dependencias compatibles con API 35

El proyecto conserva `compileSdk = 35` del Issue 2. Se eligieron versiones
estables que no obligan a migrar a API 36/37 ni a AGP 9 durante el Sprint 1.

### Binding de ApplicationContext

Hilt proporciona automáticamente `@ApplicationContext Context`. Declarar otro
provider con la misma clave produciría un binding duplicado. `AppModule`
consume el binding oficial y provee `Resources` con alcance Singleton, dejando
el contexto de aplicación disponible de forma segura para futuros providers.

### Identidad visual

El tema usa como primarios `#1B5E20` y `#81C784`, y conserva tokens semánticos
separados para presente, tardanza, falta y justificado. Dynamic Color queda
disponible pero desactivado por defecto para preservar identidad institucional.

## Validación

```text
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

El Issue está listo para PR cuando ambas tareas pasan, Hilt genera sus
componentes y el APK contiene `AsistenciaApp` y `MainActivity`.

Validación local del 20 de agosto de 2026:

- `testDebugUnitTest`: aprobado.
- `assembleDebug`: aprobado.
- KSP/Hilt: componentes generados y compilados.
- Resultado: `BUILD SUCCESSFUL`, 51 tareas ejecutadas.
