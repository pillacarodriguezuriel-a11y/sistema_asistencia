package pe.unsch.ceis.asistencia.data.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import pe.unsch.ceis.asistencia.core.coroutines.CoroutineDispatchers
import pe.unsch.ceis.asistencia.data.local.dao.ActividadDao
import pe.unsch.ceis.asistencia.data.local.entity.ActividadEntity
import pe.unsch.ceis.asistencia.data.mapper.toDomain
import pe.unsch.ceis.asistencia.data.mapper.toEntity
import pe.unsch.ceis.asistencia.domain.model.Actividad
import pe.unsch.ceis.asistencia.domain.model.ResultState
import pe.unsch.ceis.asistencia.domain.repository.ActividadRepository
import javax.inject.Inject

class ActividadRepositoryImpl
    @Inject
    constructor(
        private val actividadDao: ActividadDao,
        private val dispatchers: CoroutineDispatchers,
    ) : ActividadRepository {
        override suspend fun crear(actividad: Actividad): ResultState<Actividad> =
            repositoryCall(dispatchers.io, "No se pudo crear la actividad") {
                val id = actividadDao.insert(actividad.toEntity())
                actividad.copy(id = id)
            }

        override suspend fun obtenerPorId(id: Long): ResultState<Actividad?> =
            repositoryCall(dispatchers.io, "No se pudo obtener la actividad") {
                actividadDao.obtenerPorId(id)?.toDomain()
            }

        override suspend fun eliminar(actividad: Actividad): ResultState<Unit> =
            repositoryCall(dispatchers.io, "No se pudo eliminar la actividad") {
                check(actividadDao.eliminar(actividad.toEntity()) == 1) {
                    "La actividad ${actividad.id} no existe"
                }
            }

        override fun listar(): Flow<ResultState<List<Actividad>>> =
            actividadDao
                .obtenerTodas()
                .map<List<ActividadEntity>, ResultState<List<Actividad>>> { entities ->
                    ResultState.Success(entities.map { it.toDomain() })
                }.onStart { emit(ResultState.Loading) }
                .catch { exception ->
                    if (exception is CancellationException) throw exception
                    emit(ResultState.Error("No se pudieron listar las actividades", exception))
                }.flowOn(dispatchers.io)
    }
