package pe.unsch.ceis.asistencia.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import pe.unsch.ceis.asistencia.data.local.source.EstudianteLocalDataSource
import pe.unsch.ceis.asistencia.data.mapper.toDomain
import pe.unsch.ceis.asistencia.data.mapper.toEntity
import pe.unsch.ceis.asistencia.domain.model.Estudiante
import pe.unsch.ceis.asistencia.domain.model.ResultState
import pe.unsch.ceis.asistencia.domain.repository.EstudianteRepository

class EstudianteRepositoryImpl @Inject constructor(
    private val localDataSource: EstudianteLocalDataSource,
) : EstudianteRepository {
    override suspend fun guardarLote(
        estudiantes: List<Estudiante>,
    ): ResultState<Unit> = repositoryCall("No se pudo guardar el padrón") {
        localDataSource.guardarLote(estudiantes.map(Estudiante::toEntity))
    }

    override suspend fun buscarPorDni(
        dni: String,
    ): ResultState<Estudiante?> = repositoryCall("No se pudo buscar por DNI") {
        localDataSource.buscarPorDni(dni)?.toDomain()
    }

    override suspend fun buscarPorCodigo(
        codigo: String,
    ): ResultState<Estudiante?> = repositoryCall("No se pudo buscar por código") {
        localDataSource.buscarPorCodigo(codigo)?.toDomain()
    }

    override fun listar(): Flow<ResultState<List<Estudiante>>> =
        flow<ResultState<List<Estudiante>>> {
            emit(ResultState.Loading)
            localDataSource.observarTodos().collect { entities ->
                emit(ResultState.Success(entities.map { it.toDomain() }))
            }
        }.catch { exception ->
            emit(ResultState.Error("No se pudo listar el padrón", exception))
        }
}

