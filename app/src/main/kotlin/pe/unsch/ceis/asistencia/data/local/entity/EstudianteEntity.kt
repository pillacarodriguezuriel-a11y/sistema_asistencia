package pe.unsch.ceis.asistencia.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "estudiantes",
    indices = [
        Index(value = ["dni"], unique = true),
    ],
)
data class EstudianteEntity(
    @PrimaryKey
    val codigo: String,
    val dni: String,
    val nombresApellidos: String,
    val correoInstitucional: String,
)
