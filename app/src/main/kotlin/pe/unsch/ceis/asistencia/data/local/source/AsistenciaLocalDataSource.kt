package pe.unsch.ceis.asistencia.data.local.source

import kotlinx.coroutines.flow.Flow
import pe.unsch.ceis.asistencia.data.local.entity.RegistroAsistenciaEntity

interface AsistenciaLocalDataSource {
    /**
     * La implementación Room debe insertar atómicamente y respaldar la
     * unicidad `(actividadId, estudianteCodigo)` mediante un índice único.
     */
    suspend fun registrar(registro: RegistroAsistenciaEntity): Long

    suspend fun existeRegistro(
        actividadId: Long,
        estudianteCodigo: String,
    ): Boolean

    fun observarPorActividad(
        actividadId: Long,
    ): Flow<List<RegistroAsistenciaEntity>>
}

