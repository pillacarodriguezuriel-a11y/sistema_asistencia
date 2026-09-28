package pe.unsch.ceis.asistencia.data.local.source

import kotlinx.coroutines.flow.Flow
import pe.unsch.ceis.asistencia.data.local.entity.ActividadEntity

interface ActividadLocalDataSource {
    suspend fun crear(actividad: ActividadEntity): Long

    fun observarTodas(): Flow<List<ActividadEntity>>
}

