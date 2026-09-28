package pe.unsch.ceis.asistencia.domain.model

data class Actividad(
    val id: Long,
    val nombre: String,
    val fechaInicio: Long,
    val descripcion: String,
)
