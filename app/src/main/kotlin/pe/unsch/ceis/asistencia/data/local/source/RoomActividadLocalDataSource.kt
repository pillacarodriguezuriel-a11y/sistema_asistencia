package pe.unsch.ceis.asistencia.data.local.source

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import pe.unsch.ceis.asistencia.data.local.dao.ActividadDao
import pe.unsch.ceis.asistencia.data.local.entity.ActividadEntity

class RoomActividadLocalDataSource @Inject constructor(
    private val actividadDao: ActividadDao,
) : ActividadLocalDataSource {
    override suspend fun crear(actividad: ActividadEntity): Long =
        actividadDao.insert(actividad)

    override fun observarTodas(): Flow<List<ActividadEntity>> =
        actividadDao.obtenerTodas()
}
