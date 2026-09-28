package pe.unsch.ceis.asistencia.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import pe.unsch.ceis.asistencia.data.local.dao.ActividadDao
import pe.unsch.ceis.asistencia.data.local.dao.AsistenciaDao
import pe.unsch.ceis.asistencia.data.local.dao.EstudianteDao
import pe.unsch.ceis.asistencia.data.local.entity.ActividadEntity
import pe.unsch.ceis.asistencia.data.local.entity.AsistenciaEntity
import pe.unsch.ceis.asistencia.data.local.entity.EstudianteEntity

@Database(
    entities = [
        EstudianteEntity::class,
        ActividadEntity::class,
        AsistenciaEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun estudianteDao(): EstudianteDao

    abstract fun actividadDao(): ActividadDao

    abstract fun asistenciaDao(): AsistenciaDao
}
