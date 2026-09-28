package pe.unsch.ceis.asistencia.domain.model

data class Estudiante(
    val codigo: String,
    val dni: String,
    val nombresApellidos: String,
    val correoInstitucional: String,
)
