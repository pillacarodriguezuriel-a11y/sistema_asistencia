package pe.unsch.ceis.asistencia.data.mapper

import pe.unsch.ceis.asistencia.data.local.entity.AsistenciaEntity
import pe.unsch.ceis.asistencia.domain.model.EstadoAsistencia
import pe.unsch.ceis.asistencia.domain.model.RegistroAsistencia

fun AsistenciaEntity.toDomain(): RegistroAsistencia = RegistroAsistencia(
    id = id,
    actividadId = actividadId,
    estudianteCodigo = estudianteCodigo,
    estado = EstadoAsistencia.valueOf(estado),
    timestamp = timestamp,
)

fun RegistroAsistencia.toEntity(): AsistenciaEntity = AsistenciaEntity(
    id = id,
    actividadId = actividadId,
    estudianteCodigo = estudianteCodigo,
    estado = estado.name,
    timestamp = timestamp,
)

