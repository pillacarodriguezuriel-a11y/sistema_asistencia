# Sprint 1 — Issue 4: Contratos Clean Architecture

## Ficha

- Rol responsable: Dev Data
- Rama: `feature/sprint-1-issue-4-clean-architecture`
- Rama base temporal: `feature/sprint-1-issue-3-compose-hilt`
- Prioridad: P0
- Estado: Implementado y validado localmente; pendiente de PR

## Entregables

- Modelos Kotlin puros y `EstadoAsistencia` en Domain.
- `ResultState<T>` y contratos reactivos de repositorios.
- Entidades locales sin Room y mappers bidireccionales.
- DataSources que encapsulan la futura persistencia Room.
- Implementaciones de repositorio independientes de Android.
- `RepositoryModule` con bindings Hilt.
- Regla automatizada `verifyDomainPurity`.
- Contrato arquitectónico en `docs/architecture/CLEAN_ARCHITECTURE.md`.

## Decisiones

- Fechas y timestamps se representan como epoch milliseconds (`Long`), según
  los contratos de los Issues 1 y 3.
- Domain puede usar `kotlinx.coroutines.flow.Flow`; no usa
  `kotlinx-coroutines-android` ni tipos de plataforma.
- Los repositorios emiten `Loading` al iniciar observaciones y convierten
  errores de infraestructura en `ResultState.Error`.
- Las cancelaciones de coroutines siempre se relanzan.
- Los bindings de repositorios quedan listos; las implementaciones concretas de
  DataSources se enlazarán cuando se agreguen Room y DAOs.

## Dependencia entre ramas

El Issue 3 todavía no está integrado en `develop`. Por ello esta rama está
apilada sobre su commit. Después de aprobar el Issue 3, esta rama debe rebasarse
sobre `develop` antes de abrir o actualizar su PR definitivo.

## Criterio de cierre

```text
./gradlew verifyDomainPurity
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

El Issue queda listo para PR cuando las tres tareas pasan y el árbol Git está
limpio.

Validación local del 20 de agosto de 2026:

- `verifyDomainPurity`: aprobado.
- `testDebugUnitTest`: 4 pruebas, 0 fallos.
- `assembleDebug`: aprobado.
- Hilt/KSP: `RepositoryModule` y repositorios compilados.
- Configuration Cache: almacenada sin problemas.
- Resultado: `BUILD SUCCESSFUL`, 52 tareas verificadas.
