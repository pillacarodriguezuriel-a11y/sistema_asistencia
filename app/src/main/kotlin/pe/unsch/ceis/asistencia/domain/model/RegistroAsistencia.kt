package pe.unsch.ceis.asistencia.domain.model

data class RegistroAsistencia(
    val id: Long,
    val actividadId: Long,
    val estudianteCodigo: String,
    val estado: EstadoAsistencia,
    val timestamp: Long,
)
