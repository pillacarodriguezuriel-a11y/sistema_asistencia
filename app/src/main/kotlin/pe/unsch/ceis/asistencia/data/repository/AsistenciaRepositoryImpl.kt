package pe.unsch.ceis.asistencia.data.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import pe.unsch.ceis.asistencia.core.coroutines.CoroutineDispatchers
import pe.unsch.ceis.asistencia.data.local.dao.AsistenciaDao
import pe.unsch.ceis.asistencia.data.local.entity.AsistenciaEntity
import pe.unsch.ceis.asistencia.data.mapper.toDomain
import pe.unsch.ceis.asistencia.data.mapper.toEntity
import pe.unsch.ceis.asistencia.domain.model.RegistroAsistencia
import pe.unsch.ceis.asistencia.domain.model.ResultState
import pe.unsch.ceis.asistencia.domain.repository.AsistenciaRepository
import javax.inject.Inject

class AsistenciaRepositoryImpl
    @Inject
    constructor(
        private val asistenciaDao: AsistenciaDao,
        private val dispatchers: CoroutineDispatchers,
    ) : AsistenciaRepository {
        override suspend fun registrar(registro: RegistroAsistencia): ResultState<RegistroAsistencia> =
            repositoryCall(
                dispatchers.io,
                "No se pudo registrar la asistencia",
            ) {
                val id = asistenciaDao.registrarAsistencia(registro.toEntity())
                registro.copy(id = id)
            }

        override suspend fun existeRegistro(
            actividadId: Long,
            estudianteCodigo: String,
        ): ResultState<Boolean> =
            repositoryCall(dispatchers.io, "No se pudo verificar la asistencia") {
                asistenciaDao.existeRegistro(
                    actividadId = actividadId,
                    codigo = estudianteCodigo,
                )
            }

        override fun obtenerPorActividad(actividadId: Long): Flow<ResultState<List<RegistroAsistencia>>> =
            asistenciaDao
                .obtenerAsistenciasPorActividad(actividadId)
                .map<List<AsistenciaEntity>, ResultState<List<RegistroAsistencia>>> { entities ->
                    ResultState.Success(entities.map { it.toDomain() })
                }.onStart { emit(ResultState.Loading) }
                .catch { exception ->
                    if (exception is CancellationException) throw exception
                    emit(ResultState.Error("No se pudieron obtener las asistencias", exception))
                }.flowOn(dispatchers.io)
    }
