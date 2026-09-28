package pe.unsch.ceis.asistencia.data.local

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.os.SystemClock
import androidx.room.Room
import androidx.room.withTransaction
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import java.util.UUID
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pe.unsch.ceis.asistencia.data.local.entity.ActividadEntity
import pe.unsch.ceis.asistencia.data.local.entity.AsistenciaEntity
import pe.unsch.ceis.asistencia.data.local.entity.EstudianteEntity

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {
    private lateinit var database: AppDatabase

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java,
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun codigoAndDniAreUnique() = runBlocking {
        val dao = database.estudianteDao()
        dao.insert(estudiante(codigo = "20200001", dni = "70000001"))

        val duplicateCode = captureFailure {
            dao.insert(estudiante(codigo = "20200001", dni = "70000002"))
        }
        val duplicateDni = captureFailure {
            dao.insert(estudiante(codigo = "20200002", dni = "70000001"))
        }

        assertTrue(duplicateCode is SQLiteConstraintException)
        assertTrue(duplicateDni is SQLiteConstraintException)
        assertEquals(1, dao.contarTotal())
    }

    @Test
    fun batchInsertReplacesConflictsWithoutCreatingDuplicates() = runBlocking {
        val dao = database.estudianteDao()
        dao.insertAll(
            listOf(
                estudiante(codigo = "20200001", dni = "70000001", nombre = "Nombre inicial"),
                estudiante(codigo = "20200001", dni = "70000001", nombre = "Nombre actualizado"),
            ),
        )

        assertEquals(1, dao.contarTotal())
        assertEquals("Nombre actualizado", dao.obtenerPorCodigo("20200001")?.nombresApellidos)
    }

    @Test
    fun attendanceIsUniquePerActivityAndStudent() = runBlocking {
        val student = estudiante(codigo = "20200001", dni = "70000001")
        database.estudianteDao().insert(student)
        val actividadId = database.actividadDao().insert(actividad())
        val asistencia = asistencia(actividadId, student.codigo)
        database.asistenciaDao().registrarAsistencia(asistencia)

        val duplicate = captureFailure {
            database.asistenciaDao().registrarAsistencia(asistencia.copy(id = 0))
        }

        assertTrue(duplicate is SQLiteConstraintException)
        assertTrue(database.asistenciaDao().existeRegistro(actividadId, student.codigo))
        assertEquals(
            1,
            database.asistenciaDao().obtenerAsistenciasPorActividad(actividadId).first().size,
        )
    }

    @Test
    fun deletingActivityCascadesAttendance() = runBlocking {
        val student = estudiante(codigo = "20200001", dni = "70000001")
        database.estudianteDao().insert(student)
        val actividadId = database.actividadDao().insert(actividad())
        database.asistenciaDao().registrarAsistencia(asistencia(actividadId, student.codigo))

        val deleted = database.actividadDao().eliminar(
            requireNotNull(database.actividadDao().obtenerPorId(actividadId)),
        )

        assertEquals(1, deleted)
        assertFalse(database.asistenciaDao().existeRegistro(actividadId, student.codigo))
    }

    @Test
    fun joinReturnsStudentAndAttendanceData() = runBlocking {
        val student = estudiante(codigo = "20200001", dni = "70000001")
        database.estudianteDao().insert(student)
        val actividadId = database.actividadDao().insert(actividad())
        database.asistenciaDao().registrarAsistencia(asistencia(actividadId, student.codigo))

        val result = database.asistenciaDao()
            .obtenerAsistenciasConEstudiante(actividadId)
            .first()
            .single()

        assertEquals(student.codigo, result.estudianteCodigo)
        assertEquals(student.nombresApellidos, result.nombresApellidos)
        assertEquals(student.dni, result.dni)
        assertEquals("PRESENTE", result.estado)
    }

    @Test
    fun transactionRollsBackAllWritesOnFailure() = runBlocking {
        val student = estudiante(codigo = "20200001", dni = "70000001")

        val failure = captureFailure {
            database.withTransaction {
                database.estudianteDao().insert(student)
                database.asistenciaDao().registrarAsistencia(
                    asistencia(actividadId = Long.MAX_VALUE, codigo = student.codigo),
                )
            }
        }

        assertTrue(failure is SQLiteConstraintException)
        assertNull(database.estudianteDao().obtenerPorCodigo(student.codigo))
    }

    @Test
    fun warmedSingleWritesStayBelowFiftyMilliseconds() = runBlocking {
        val dao = database.estudianteDao()
        repeat(WARMUP_WRITES) { index ->
            dao.insert(uniqueStudent(index))
        }

        val durationsMillis = List(MEASURED_WRITES) { offset ->
            val startedAt = SystemClock.elapsedRealtimeNanos()
            dao.insert(uniqueStudent(WARMUP_WRITES + offset))
            (SystemClock.elapsedRealtimeNanos() - startedAt) / NANOS_PER_MILLISECOND
        }.sorted()
        val percentile95 = durationsMillis[
            ((durationsMillis.size * 95 + 99) / 100 - 1).coerceAtLeast(0)
        ]

        assertTrue(
            "P95 de escritura: ${percentile95}ms; máximo permitido: ${MAX_WRITE_MILLIS}ms",
            percentile95 < MAX_WRITE_MILLIS,
        )
    }

    private suspend fun captureFailure(block: suspend () -> Unit): Throwable? =
        runCatching { block() }.exceptionOrNull()

    private fun estudiante(
        codigo: String,
        dni: String,
        nombre: String = "Estudiante de prueba",
    ) = EstudianteEntity(
        codigo = codigo,
        dni = dni,
        nombresApellidos = nombre,
        correoInstitucional = "$codigo@unsch.edu.pe",
    )

    private fun uniqueStudent(index: Int): EstudianteEntity {
        val suffix = index.toString().padStart(6, '0')
        return estudiante(
            codigo = "20$suffix",
            dni = "70$suffix",
            nombre = "Estudiante $suffix ${UUID.randomUUID()}",
        )
    }

    private fun actividad() = ActividadEntity(
        nombre = "Asamblea",
        fechaInicio = 1_700_000_000_000,
        descripcion = "Actividad de prueba",
    )

    private fun asistencia(
        actividadId: Long,
        codigo: String,
    ) = AsistenciaEntity(
        actividadId = actividadId,
        estudianteCodigo = codigo,
        estado = "PRESENTE",
        timestamp = 1_700_000_000_100,
    )

    private companion object {
        const val WARMUP_WRITES = 5
        const val MEASURED_WRITES = 30
        const val MAX_WRITE_MILLIS = 50L
        const val NANOS_PER_MILLISECOND = 1_000_000L
    }
}
