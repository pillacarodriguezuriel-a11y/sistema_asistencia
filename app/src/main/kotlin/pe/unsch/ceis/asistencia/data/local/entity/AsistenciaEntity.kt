package pe.unsch.ceis.asistencia.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "asistencias",
    foreignKeys = [
        ForeignKey(
            entity = ActividadEntity::class,
            parentColumns = ["id"],
            childColumns = ["actividadId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = EstudianteEntity::class,
            parentColumns = ["codigo"],
            childColumns = ["estudianteCodigo"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(
            value = ["actividadId", "estudianteCodigo"],
            unique = true,
        ),
        Index(value = ["estudianteCodigo"]),
    ],
)
data class AsistenciaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val actividadId: Long,
    val estudianteCodigo: String,
    val estado: String,
    val timestamp: Long,
) {
    init {
        require(estado in ESTADOS_VALIDOS) {
            "Estado de asistencia no válido: $estado"
        }
    }

    companion object {
        val ESTADOS_VALIDOS: Set<String> = setOf(
            "PRESENTE",
            "TARDANZA",
            "FALTA",
            "JUSTIFICADO",
        )
    }
}
