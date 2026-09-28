package pe.unsch.ceis.asistencia.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import pe.unsch.ceis.asistencia.data.local.AppDatabase
import pe.unsch.ceis.asistencia.data.local.dao.ActividadDao
import pe.unsch.ceis.asistencia.data.local.dao.AsistenciaDao
import pe.unsch.ceis.asistencia.data.local.dao.EstudianteDao

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private const val DATABASE_NAME = "asistencia_unsch.db"

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DATABASE_NAME,
    ).build()

    @Provides
    @Singleton
    fun provideEstudianteDao(database: AppDatabase): EstudianteDao =
        database.estudianteDao()

    @Provides
    @Singleton
    fun provideActividadDao(database: AppDatabase): ActividadDao =
        database.actividadDao()

    @Provides
    @Singleton
    fun provideAsistenciaDao(database: AppDatabase): AsistenciaDao =
        database.asistenciaDao()
}
