package pe.unsch.ceis.asistencia.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import pe.unsch.ceis.asistencia.data.local.source.ActividadLocalDataSource
import pe.unsch.ceis.asistencia.data.local.source.AsistenciaLocalDataSource
import pe.unsch.ceis.asistencia.data.local.source.EstudianteLocalDataSource
import pe.unsch.ceis.asistencia.data.local.source.RoomActividadLocalDataSource
import pe.unsch.ceis.asistencia.data.local.source.RoomAsistenciaLocalDataSource
import pe.unsch.ceis.asistencia.data.local.source.RoomEstudianteLocalDataSource

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindEstudianteLocalDataSource(
        implementation: RoomEstudianteLocalDataSource,
    ): EstudianteLocalDataSource

    @Binds
    @Singleton
    abstract fun bindActividadLocalDataSource(
        implementation: RoomActividadLocalDataSource,
    ): ActividadLocalDataSource

    @Binds
    @Singleton
    abstract fun bindAsistenciaLocalDataSource(
        implementation: RoomAsistenciaLocalDataSource,
    ): AsistenciaLocalDataSource
}
