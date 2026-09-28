# Sprint 2 — Issue 13: Repositorios y enlace con Room

## Ficha

- Rol responsable: Dev Mobile
- Estado: Implementado y validado localmente; pendiente de PR
- Prioridad: P0

## Implementación

- Mappers bidireccionales `Estudiante`, `Actividad` y `RegistroAsistencia`
  entre Domain y las entidades Room.
- Repositorios concretos conectados directamente con sus DAOs.
- Operaciones suspendidas ejecutadas mediante el dispatcher `io` inyectable.
- Flujos Room transformados a modelos de dominio, emitidos como
  `ResultState.Loading`, `Success` o `Error` y ejecutados con `flowOn(io)`.
- Cancelaciones de corrutinas propagadas sin convertirlas en errores de
  aplicación.
- Enlaces `@Binds` y `@Singleton` para los tres repositorios en Hilt.

Los antiguos adaptadores intermedios de DataSource se retiraron para evitar
duplicar abstracciones: Domain continúa dependiendo solo de sus contratos y
Data es la única capa que conoce Room.

## Criterios verificados

- Domain no importa Android, Room, Hilt, Data ni UI.
- Data no depende de UI o del paquete DI.
- Hilt genera correctamente el grafo con DAOs, dispatchers y repositorios.
- `testDebugUnitTest` y las compilaciones Debug/AndroidTest finalizan sin
  errores.
