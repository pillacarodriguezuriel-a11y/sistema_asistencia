package pe.unsch.ceis.asistencia.data.local.entity

/**
 * Contrato local preparado para recibir anotaciones Room en el Issue de BD.
 * Se mantiene sin Room para que este Issue defina únicamente las fronteras.
 */
data class EstudianteEntity(
    val codigo: String,
    val dni: String,
    val nombresApellidos: String,
    val correoInstitucional: String,
)

