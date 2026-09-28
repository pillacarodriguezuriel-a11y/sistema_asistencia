# Sprint 2 — Issue 10: Estudiante y EstudianteDao

## Ficha

- Rol responsable: Dev Data
- Estado: Implementado y validado por Room/KSP; prueba en dispositivo pendiente
- Prioridad: P0

## Entregables

- Tabla `estudiantes` con `codigo` como clave primaria e índice único para
  `dni`.
- Columnas `codigo`, `dni`, `nombresApellidos` y `correoInstitucional`.
- Inserción individual con conflicto abortable e inserción masiva con
  `OnConflictStrategy.REPLACE`.
- Consultas por código/DNI, listado reactivo, búsqueda por los cuatro campos y
  conteo total.

## Reglas

La importación por lote actualiza conflictos sin crear filas duplicadas. La
inserción individual conserva la estrategia `ABORT` para que los flujos
interactivos puedan informar el conflicto en lugar de reemplazarlo en silencio.
