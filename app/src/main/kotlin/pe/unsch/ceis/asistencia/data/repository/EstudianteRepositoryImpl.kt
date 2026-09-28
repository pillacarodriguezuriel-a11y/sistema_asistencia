package pe.unsch.ceis.asistencia.data.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import pe.unsch.ceis.asistencia.core.coroutines.CoroutineDispatchers
import pe.unsch.ceis.asistencia.data.local.dao.EstudianteDao
import pe.unsch.ceis.asistencia.data.local.entity.EstudianteEntity
import pe.unsch.ceis.asistencia.data.mapper.toDomain
import pe.unsch.ceis.asistencia.data.mapper.toEntity
import pe.unsch.ceis.asistencia.domain.model.Estudiante
import pe.unsch.ceis.asistencia.domain.model.ResultState
import pe.unsch.ceis.asistencia.domain.repository.EstudianteRepository
import javax.inject.Inject

class EstudianteRepositoryImpl
    @Inject
    constructor(
        private val estudianteDao: EstudianteDao,
        private val dispatchers: CoroutineDispatchers,
    ) : EstudianteRepository {
        override suspend fun guardarLote(estudiantes: List<Estudiante>): ResultState<Unit> =
            repositoryCall(dispatchers.io, "No se pudo guardar el padrón") {
                estudianteDao.insertAll(estudiantes.map(Estudiante::toEntity))
            }

        override suspend fun buscarPorDni(dni: String): ResultState<Estudiante?> =
            repositoryCall(dispatchers.io, "No se pudo buscar por DNI") {
                estudianteDao.obtenerPorDni(dni)?.toDomain()
            }

        override suspend fun buscarPorCodigo(codigo: String): ResultState<Estudiante?> =
            repositoryCall(dispatchers.io, "No se pudo buscar por código") {
                estudianteDao.obtenerPorCodigo(codigo)?.toDomain()
            }

        override fun listar(): Flow<ResultState<List<Estudiante>>> =
            estudianteDao
                .obtenerTodos()
                .map<List<EstudianteEntity>, ResultState<List<Estudiante>>> { entities ->
                    ResultState.Success(entities.map { it.toDomain() })
                }.onStart { emit(ResultState.Loading) }
                .catch { exception ->
                    if (exception is CancellationException) throw exception
                    emit(ResultState.Error("No se pudo listar el padrón", exception))
                }.flowOn(dispatchers.io)
    }
