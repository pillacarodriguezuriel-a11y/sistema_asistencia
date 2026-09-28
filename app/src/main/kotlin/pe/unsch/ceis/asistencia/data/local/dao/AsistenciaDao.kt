package pe.unsch.ceis.asistencia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pe.unsch.ceis.asistencia.data.local.entity.AsistenciaEntity
import pe.unsch.ceis.asistencia.data.local.model.AsistenciaConEstudiante

@Dao
interface AsistenciaDao {
    @Insert
    suspend fun registrarAsistencia(asistencia: AsistenciaEntity): Long

    @Query(
        """
        SELECT EXISTS(
            SELECT 1
            FROM asistencias
            WHERE actividadId = :actividadId
              AND estudianteCodigo = :codigo
        )
        """,
    )
    suspend fun existeRegistro(
        actividadId: Long,
        codigo: String,
    ): Boolean

    @Query(
        """
        SELECT *
        FROM asistencias
        WHERE actividadId = :actividadId
        ORDER BY timestamp ASC
        """,
    )
    fun obtenerAsistenciasPorActividad(actividadId: Long): Flow<List<AsistenciaEntity>>

    @Query(
        """
        SELECT
            a.id AS asistenciaId,
            a.actividadId AS actividadId,
            a.estudianteCodigo AS estudianteCodigo,
            e.nombresApellidos AS nombresApellidos,
            e.dni AS dni,
            e.correoInstitucional AS correoInstitucional,
            a.estado AS estado,
            a.timestamp AS timestamp
        FROM asistencias AS a
        INNER JOIN estudiantes AS e
            ON e.codigo = a.estudianteCodigo
        WHERE a.actividadId = :actividadId
        ORDER BY a.timestamp ASC
        """,
    )
    fun obtenerAsistenciasConEstudiante(actividadId: Long): Flow<List<AsistenciaConEstudiante>>
}
