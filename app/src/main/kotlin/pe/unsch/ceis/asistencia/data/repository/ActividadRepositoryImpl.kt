package pe.unsch.ceis.asistencia.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import pe.unsch.ceis.asistencia.data.local.source.ActividadLocalDataSource
import pe.unsch.ceis.asistencia.data.mapper.toDomain
import pe.unsch.ceis.asistencia.data.mapper.toEntity
import pe.unsch.ceis.asistencia.domain.model.Actividad
import pe.unsch.ceis.asistencia.domain.model.ResultState
import pe.unsch.ceis.asistencia.domain.repository.ActividadRepository

class ActividadRepositoryImpl @Inject constructor(
    private val localDataSource: ActividadLocalDataSource,
) : ActividadRepository {
    override suspend fun crear(
        actividad: Actividad,
    ): ResultState<Actividad> = repositoryCall("No se pudo crear la actividad") {
        val id = localDataSource.crear(actividad.toEntity())
        actividad.copy(id = id)
    }

    override fun listar(): Flow<ResultState<List<Actividad>>> =
        flow<ResultState<List<Actividad>>> {
            emit(ResultState.Loading)
            localDataSource.observarTodas().collect { entities ->
                emit(ResultState.Success(entities.map { it.toDomain() }))
            }
        }.catch { exception ->
            emit(ResultState.Error("No se pudieron listar las actividades", exception))
        }
}

