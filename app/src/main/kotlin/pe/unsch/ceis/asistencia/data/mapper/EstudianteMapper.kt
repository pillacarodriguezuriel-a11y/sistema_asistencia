package pe.unsch.ceis.asistencia.data.mapper

import pe.unsch.ceis.asistencia.data.local.entity.EstudianteEntity
import pe.unsch.ceis.asistencia.domain.model.Estudiante

fun EstudianteEntity.toDomain(): Estudiante =
    Estudiante(
        codigo = codigo,
        dni = dni,
        nombresApellidos = nombresApellidos,
        correoInstitucional = correoInstitucional,
    )

fun Estudiante.toEntity(): EstudianteEntity =
    EstudianteEntity(
        codigo = codigo,
        dni = dni,
        nombresApellidos = nombresApellidos,
        correoInstitucional = correoInstitucional,
    )
