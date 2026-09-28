# Sprint 2 — Issue 11: Actividades, asistencias y DAOs

## Ficha

- Rol responsable: Dev Data
- Estado: Implementado y validado por Room/KSP; prueba en dispositivo pendiente
- Prioridad: P0

## Entregables

- Tablas `actividades` y `asistencias` con identificadores autogenerados.
- Claves foráneas hacia actividad y estudiante con `ON DELETE CASCADE`.
- Índice único `actividadId + estudianteCodigo` contra doble marcado.
- Índice auxiliar de la clave foránea `estudianteCodigo` para evitar barridos
  completos al modificar estudiantes.
- DAOs para crear/eliminar/listar actividades y registrar/consultar
  asistencias.
- Proyección `AsistenciaConEstudiante` obtenida mediante `INNER JOIN`.

Los estados persistidos admitidos por Domain son `PRESENTE`, `TARDANZA`,
`FALTA` y `JUSTIFICADO`.
