package pe.unsch.ceis.asistencia.data.local.source

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import pe.unsch.ceis.asistencia.data.local.dao.EstudianteDao
import pe.unsch.ceis.asistencia.data.local.entity.EstudianteEntity

class RoomEstudianteLocalDataSource @Inject constructor(
    private val estudianteDao: EstudianteDao,
) : EstudianteLocalDataSource {
    override suspend fun guardarLote(estudiantes: List<EstudianteEntity>) {
        estudianteDao.insertAll(estudiantes)
    }

    override suspend fun buscarPorDni(dni: String): EstudianteEntity? =
        estudianteDao.obtenerPorDni(dni)

    override suspend fun buscarPorCodigo(codigo: String): EstudianteEntity? =
        estudianteDao.obtenerPorCodigo(codigo)

    override fun observarTodos(): Flow<List<EstudianteEntity>> =
        estudianteDao.obtenerTodos()
}
