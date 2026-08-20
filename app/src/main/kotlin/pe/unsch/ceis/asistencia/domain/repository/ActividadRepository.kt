package pe.unsch.ceis.asistencia.domain.repository

import kotlinx.coroutines.flow.Flow
import pe.unsch.ceis.asistencia.domain.model.Actividad
import pe.unsch.ceis.asistencia.domain.model.ResultState

interface ActividadRepository {
    suspend fun crear(actividad: Actividad): ResultState<Actividad>

    fun listar(): Flow<ResultState<List<Actividad>>>
}

