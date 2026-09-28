package pe.unsch.ceis.asistencia.data.local

import android.database.sqlite.SQLiteConstraintException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import pe.unsch.ceis.asistencia.data.local.entity.ActividadEntity
import pe.unsch.ceis.asistencia.data.local.entity.AsistenciaEntity
import pe.unsch.ceis.asistencia.data.local.entity.EstudianteEntity

class AsistenciaDaoTest : BaseDaoTest() {
    @Test
    fun `foreign key rejects attendance for unknown student`() =
        runBlocking {
            val actividadId = database.actividadDao().insert(actividad())

            val failure =
                runCatching {
                    database.asistenciaDao().registrarAsistencia(
                        asistencia(actividadId, "NO-EXISTE"),
                    )
                }.exceptionOrNull()

            assertTrue(failure is SQLiteConstraintException)
        }

    @Test
    fun `unique composite index rejects duplicate attendance`() =
        runBlocking {
            val student = estudiante()
            database.estudianteDao().insert(student)
            val actividadId = database.actividadDao().insert(actividad())
            val attendance = asistencia(actividadId, student.codigo)
            database.asistenciaDao().registrarAsistencia(attendance)

            val failure =
                runCatching {
                    database.asistenciaDao().registrarAsistencia(attendance.copy(id = 0))
                }.exceptionOrNull()

            assertTrue(failure is SQLiteConstraintException)
            assertEquals(
                1,
                database
                    .asistenciaDao()
                    .obtenerAsistenciasPorActividad(actividadId)
                    .first()
                    .size,
            )
        }

    @Test
    fun `deleting activity cascades all its attendance`() =
        runBlocking {
            val student = estudiante()
            database.estudianteDao().insert(student)
            val actividadId = database.actividadDao().insert(actividad())
            database.asistenciaDao().registrarAsistencia(asistencia(actividadId, student.codigo))

            database.actividadDao().eliminar(
                requireNotNull(database.actividadDao().obtenerPorId(actividadId)),
            )

            assertFalse(database.asistenciaDao().existeRegistro(actividadId, student.codigo))
            assertTrue(
                database
                    .asistenciaDao()
                    .obtenerAsistenciasPorActividad(actividadId)
                    .first()
                    .isEmpty(),
            )
        }

    private fun estudiante() =
        EstudianteEntity(
            codigo = "20200001",
            dni = "70000001",
            nombresApellidos = "Estudiante de prueba",
            correoInstitucional = "20200001@unsch.edu.pe",
        )

    private fun actividad() =
        ActividadEntity(
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
}
