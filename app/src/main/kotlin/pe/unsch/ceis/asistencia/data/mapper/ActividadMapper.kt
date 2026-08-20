package pe.unsch.ceis.asistencia.data.mapper

import pe.unsch.ceis.asistencia.data.local.entity.ActividadEntity
import pe.unsch.ceis.asistencia.domain.model.Actividad

fun ActividadEntity.toDomain(): Actividad = Actividad(
    id = id,
    nombre = nombre,
    fechaInicio = fechaInicio,
    descripcion = descripcion,
)

fun Actividad.toEntity(): ActividadEntity = ActividadEntity(
    id = id,
    nombre = nombre,
    fechaInicio = fechaInicio,
    descripcion = descripcion,
)

