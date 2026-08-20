package pe.unsch.ceis.asistencia.data.local.entity

data class RegistroAsistenciaEntity(
    val id: Long,
    val actividadId: Long,
    val estudianteCodigo: String,
    val estado: String,
    val timestamp: Long,
)

