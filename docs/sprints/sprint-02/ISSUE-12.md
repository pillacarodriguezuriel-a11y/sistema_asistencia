# Sprint 2 — Issue 12: AppDatabase, Hilt y migraciones

## Ficha

- Rol responsable: Dev Data
- Estado: Implementado y validado localmente; pendiente de PR
- Prioridad: P0

## Entregables

- `AppDatabase` versión 1 con exportación de esquema habilitada.
- Plugin Room 2.8.5 y compilador KSP.
- Esquema JSON versionado en `app/schemas`.
- `DatabaseModule` con una base singleton llamada `asistencia_unsch.db` y
  proveedores para los tres DAOs.
- Adaptadores Room para los DataSources y enlaces Hilt en
  `LocalDataSourceModule`.

## Política de migraciones

La versión 1 es el baseline. Cualquier cambio posterior debe incrementar la
versión, aportar una migración explícita y probarla usando los esquemas
exportados. No se permite destrucción automática de datos como estrategia de
producción.

## Comandos de verificación

```text
./gradlew testDebugUnitTest
./gradlew assembleDebugAndroidTest
./gradlew assembleDebug
./gradlew assembleRelease
```

Validación local del 28 de septiembre de 2026:

- Room/KSP: consultas, entidades e índices compilados correctamente.
- Esquema `AppDatabase/1.json`: generado y validado por prueba JVM.
- `testDebugUnitTest`: 8 pruebas, 0 fallos, 0 errores, 0 omitidas.
- `assembleDebugAndroidTest`: aprobado; siete pruebas instrumentadas compiladas.
- `assembleDebug`: aprobado.
- `lintDebug`: aprobado sin errores.
- `assembleRelease`: aprobado; APK minificada de 1 796 987 bytes (1.71 MiB).
- SHA-256 release: `852D76ADB9B5C9752B611248314972C4EF831F39C2B6F4AE7D965D8D78489C38`.
- Pruebas instrumentadas no ejecutadas: ADB no reportó dispositivos conectados.
