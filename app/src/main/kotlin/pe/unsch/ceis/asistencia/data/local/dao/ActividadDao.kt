package pe.unsch.ceis.asistencia.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pe.unsch.ceis.asistencia.data.local.entity.ActividadEntity

@Dao
interface ActividadDao {
    @Insert
    suspend fun insert(actividad: ActividadEntity): Long

    @Delete
    suspend fun eliminar(actividad: ActividadEntity): Int

    @Query("SELECT * FROM actividades WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Long): ActividadEntity?

    @Query("SELECT * FROM actividades ORDER BY fechaInicio DESC")
    fun obtenerTodas(): Flow<List<ActividadEntity>>
}
