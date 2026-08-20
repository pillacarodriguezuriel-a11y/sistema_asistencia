package pe.unsch.ceis.asistencia.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import pe.unsch.ceis.asistencia.data.local.source.AsistenciaLocalDataSource
import pe.unsch.ceis.asistencia.data.mapper.toDomain
import pe.unsch.ceis.asistencia.data.mapper.toEntity
import pe.unsch.ceis.asistencia.domain.model.RegistroAsistencia
import pe.unsch.ceis.asistencia.domain.model.ResultState
import pe.unsch.ceis.asistencia.domain.repository.AsistenciaRepository

class AsistenciaRepositoryImpl @Inject constructor(
    private val localDataSource: AsistenciaLocalDataSource,
) : AsistenciaRepository {
    override suspend fun registrar(
        registro: RegistroAsistencia,
    ): ResultState<RegistroAsistencia> = repositoryCall("No se pudo registrar la asistencia") {
        val id = localDataSource.registrar(registro.toEntity())
        registro.copy(id = id)
    }

    override suspend fun existeRegistro(
        actividadId: Long,
        estudianteCodigo: String,
    ): ResultState<Boolean> = repositoryCall("No se pudo verificar la asistencia") {
        localDataSource.existeRegistro(
            actividadId = actividadId,
            estudianteCodigo = estudianteCodigo,
        )
    }

    override fun obtenerPorActividad(
        actividadId: Long,
    ): Flow<ResultState<List<RegistroAsistencia>>> =
        flow<ResultState<List<RegistroAsistencia>>> {
            emit(ResultState.Loading)
            localDataSource.observarPorActividad(actividadId).collect { entities ->
                emit(ResultState.Success(entities.map { it.toDomain() }))
            }
        }.catch { exception ->
            emit(ResultState.Error("No se pudieron obtener las asistencias", exception))
        }
}

