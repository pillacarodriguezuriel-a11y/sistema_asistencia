# Contrato de Clean Architecture

## Regla de dependencias

```text
UI ───────────────> Domain <────────────── Data
                              ▲
                              │
                         DI compone
```

- `domain`: modelos, resultados, casos de uso y contratos. Solo Kotlin y
  coroutines-core (`Flow`); no conoce Android, Room, Compose, Hilt ni Data.
- `data`: entidades locales, mappers, DataSources e implementaciones de los
  contratos de Domain. Room será un adapter detrás de `data/local/source`.
- `ui`: consume contratos/casos de uso de Domain y transforma resultados en
  estado de pantalla. No accede a entidades locales ni DataSources.
- `di`: composition root. Es la única capa que enlaza interfaces de Domain con
  implementaciones de Data mediante Hilt.

## Contratos de persistencia

Las entidades actuales son DTOs Kotlin preparados para recibir anotaciones
Room en un Issue posterior. Los mappers son el único lugar que convierte entre
esas entidades y los modelos de dominio.

Los DataSources exponen operaciones compatibles con futuros DAOs:

- inserción/actualización masiva de estudiantes;
- búsquedas por DNI y código;
- creación/listado de actividades;
- registro, comprobación y observación de asistencias por actividad.

La futura implementación Room debe imponer la unicidad
`(actividadId, estudianteCodigo)` en la base de datos. Consultar antes de
insertar sirve para la UI, pero no reemplaza esa restricción transaccional.

## Estados y errores

`ResultState<T>` separa `Loading`, `Success` y `Error`. Los repositorios
transforman excepciones de infraestructura en `Error`, pero nunca absorben
`CancellationException`, preservando la cancelación estructurada de coroutines.

## Verificación automática

La tarea `verifyDomainPurity` inspecciona los imports de `domain/` y falla si
encuentra Android, AndroidX, Dagger/Hilt o referencias hacia `data`, `di` o
`ui`. La tarea `verifyLayerBoundaries` impide referencias `ui -> data/di`,
`data -> ui/di` y cualquier acoplamiento de `core` con las capas de la
aplicación. Ambas están conectadas a `testDebugUnitTest` y `check`.

```text
./gradlew verifyDomainPurity
./gradlew verifyLayerBoundaries
./gradlew testDebugUnitTest
```
