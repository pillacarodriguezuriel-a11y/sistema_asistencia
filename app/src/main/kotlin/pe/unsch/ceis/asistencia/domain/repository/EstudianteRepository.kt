package pe.unsch.ceis.asistencia.domain.repository

import kotlinx.coroutines.flow.Flow
import pe.unsch.ceis.asistencia.domain.model.Estudiante
import pe.unsch.ceis.asistencia.domain.model.ResultState

interface EstudianteRepository {
    suspend fun guardarLote(estudiantes: List<Estudiante>): ResultState<Unit>

    suspend fun buscarPorDni(dni: String): ResultState<Estudiante?>

    suspend fun buscarPorCodigo(codigo: String): ResultState<Estudiante?>

    fun listar(): Flow<ResultState<List<Estudiante>>>
}

