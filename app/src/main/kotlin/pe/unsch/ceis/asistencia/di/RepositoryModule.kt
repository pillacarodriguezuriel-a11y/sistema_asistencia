package pe.unsch.ceis.asistencia.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import pe.unsch.ceis.asistencia.data.repository.ActividadRepositoryImpl
import pe.unsch.ceis.asistencia.data.repository.AsistenciaRepositoryImpl
import pe.unsch.ceis.asistencia.data.repository.EstudianteRepositoryImpl
import pe.unsch.ceis.asistencia.domain.repository.ActividadRepository
import pe.unsch.ceis.asistencia.domain.repository.AsistenciaRepository
import pe.unsch.ceis.asistencia.domain.repository.EstudianteRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindEstudianteRepository(
        implementation: EstudianteRepositoryImpl,
    ): EstudianteRepository

    @Binds
    @Singleton
    abstract fun bindAsistenciaRepository(
        implementation: AsistenciaRepositoryImpl,
    ): AsistenciaRepository

    @Binds
    @Singleton
    abstract fun bindActividadRepository(
        implementation: ActividadRepositoryImpl,
    ): ActividadRepository
}

