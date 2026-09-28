package pe.unsch.ceis.asistencia.data.local.model

data class AsistenciaConEstudiante(
    val asistenciaId: Long,
    val actividadId: Long,
    val estudianteCodigo: String,
    val nombresApellidos: String,
    val dni: String,
    val correoInstitucional: String,
    val estado: String,
    val timestamp: Long,
)
