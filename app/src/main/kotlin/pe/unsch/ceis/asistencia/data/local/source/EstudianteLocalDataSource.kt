package pe.unsch.ceis.asistencia.data.local.source

import kotlinx.coroutines.flow.Flow
import pe.unsch.ceis.asistencia.data.local.entity.EstudianteEntity

interface EstudianteLocalDataSource {
    suspend fun guardarLote(estudiantes: List<EstudianteEntity>)

    suspend fun buscarPorDni(dni: String): EstudianteEntity?

    suspend fun buscarPorCodigo(codigo: String): EstudianteEntity?

    fun observarTodos(): Flow<List<EstudianteEntity>>
}

