package pe.unsch.ceis.asistencia.domain.repository

import kotlinx.coroutines.flow.Flow
import pe.unsch.ceis.asistencia.domain.model.RegistroAsistencia
import pe.unsch.ceis.asistencia.domain.model.ResultState

interface AsistenciaRepository {
    suspend fun registrar(
        registro: RegistroAsistencia,
    ): ResultState<RegistroAsistencia>

    suspend fun existeRegistro(
        actividadId: Long,
        estudianteCodigo: String,
    ): ResultState<Boolean>

    fun obtenerPorActividad(
        actividadId: Long,
    ): Flow<ResultState<List<RegistroAsistencia>>>
}

