# Sprint 2 — Issue 16: Calidad estática con Ktlint

## Ficha

- Rol responsable: DevOps
- Estado: Implementado y validado localmente; pendiente de PR
- Prioridad: P0

## Configuración

- Plugin Gradle `org.jlleitschuh.gradle.ktlint` 14.2.0.
- Motor Ktlint 1.5.0 y estilo oficial para Kotlin/Kotlin DSL.
- Límite de línea de 120 caracteres y excepción declarativa para nombres de
  funciones anotadas con `@Composable`.
- `ignoreFailures = false`: no se aceptan infracciones ni baseline silencioso.
- Código generado y directorios de compilación excluidos del análisis.

## Integración continua

`android_ci.yml` ejecuta `./gradlew --no-daemon ktlintCheck` antes de pruebas y
compilación. Cualquier formato inválido, importación desordenada/no utilizada o
regla de sintaxis incumplida devuelve un código distinto de cero y bloquea el
job, por lo que la protección de rama puede impedir el merge.

## Comandos locales

```text
./gradlew ktlintCheck
./gradlew ktlintFormat
```

La corrección automática es una ayuda local; el pipeline solo verifica y nunca
modifica el código del pull request.

## Resultado local

Validación del 28 de septiembre de 2026:

- `ktlintCheck`: aprobado.
- `testDebugUnitTest`: 17 pruebas aprobadas.
- `assembleDebug` y `assembleDebugAndroidTest`: aprobados.
- `lintDebug`: 0 errores; 11 advertencias informativas preexistentes sobre
  versiones disponibles y metadatos de aplicación.
- `assembleRelease`: aprobado con R8; APK de 1,71 MiB, dentro de la meta de
  15 MiB.
