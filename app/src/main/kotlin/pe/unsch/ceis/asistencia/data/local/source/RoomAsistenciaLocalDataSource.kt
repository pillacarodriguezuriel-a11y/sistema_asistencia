package pe.unsch.ceis.asistencia.data.local.source

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import pe.unsch.ceis.asistencia.data.local.dao.AsistenciaDao
import pe.unsch.ceis.asistencia.data.local.entity.AsistenciaEntity

class RoomAsistenciaLocalDataSource @Inject constructor(
    private val asistenciaDao: AsistenciaDao,
) : AsistenciaLocalDataSource {
    override suspend fun registrar(registro: AsistenciaEntity): Long =
        asistenciaDao.registrarAsistencia(registro)

    override suspend fun existeRegistro(
        actividadId: Long,
        estudianteCodigo: String,
    ): Boolean = asistenciaDao.existeRegistro(
        actividadId = actividadId,
        codigo = estudianteCodigo,
    )

    override fun observarPorActividad(
        actividadId: Long,
    ): Flow<List<AsistenciaEntity>> =
        asistenciaDao.obtenerAsistenciasPorActividad(actividadId)
}
