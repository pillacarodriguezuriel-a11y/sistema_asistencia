package pe.unsch.ceis.asistencia.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pe.unsch.ceis.asistencia.data.local.entity.EstudianteEntity

@Dao
interface EstudianteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(estudiantes: List<EstudianteEntity>)

    @Insert
    suspend fun insert(estudiante: EstudianteEntity): Long

    @Query("SELECT * FROM estudiantes WHERE codigo = :codigo LIMIT 1")
    suspend fun obtenerPorCodigo(codigo: String): EstudianteEntity?

    @Query("SELECT * FROM estudiantes WHERE dni = :dni LIMIT 1")
    suspend fun obtenerPorDni(dni: String): EstudianteEntity?

    @Query("SELECT * FROM estudiantes ORDER BY nombresApellidos COLLATE NOCASE ASC")
    fun obtenerTodos(): Flow<List<EstudianteEntity>>

    @Query(
        """
        SELECT *
        FROM estudiantes
        WHERE codigo LIKE '%' || :filtro || '%' COLLATE NOCASE
           OR dni LIKE '%' || :filtro || '%' COLLATE NOCASE
           OR nombresApellidos LIKE '%' || :filtro || '%' COLLATE NOCASE
           OR correoInstitucional LIKE '%' || :filtro || '%' COLLATE NOCASE
        ORDER BY nombresApellidos COLLATE NOCASE ASC
        """,
    )
    fun buscarEstudiantes(filtro: String): Flow<List<EstudianteEntity>>

    @Query("SELECT COUNT(*) FROM estudiantes")
    suspend fun contarTotal(): Int
}
