package pe.unsch.ceis.asistencia.data.local

import android.database.sqlite.SQLiteConstraintException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import pe.unsch.ceis.asistencia.data.local.entity.EstudianteEntity

class EstudianteDaoTest : BaseDaoTest() {
    @Test
    fun `insert rejects duplicate student code`() =
        runBlocking {
            val dao = database.estudianteDao()
            dao.insert(estudiante(codigo = "20200001", dni = "70000001"))

            val failure =
                runCatching {
                    dao.insert(estudiante(codigo = "20200001", dni = "70000002"))
                }.exceptionOrNull()

            assertTrue(failure is SQLiteConstraintException)
            assertEquals(1, dao.contarTotal())
        }

    @Test
    fun `insert rejects duplicate dni`() =
        runBlocking {
            val dao = database.estudianteDao()
            dao.insert(estudiante(codigo = "20200001", dni = "70000001"))

            val failure =
                runCatching {
                    dao.insert(estudiante(codigo = "20200002", dni = "70000001"))
                }.exceptionOrNull()

            assertTrue(failure is SQLiteConstraintException)
            assertEquals(1, dao.contarTotal())
        }

    @Test
    fun `batch insert replaces an existing student`() =
        runBlocking {
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
    fun `batch insert replaces the previous row when dni belongs to another code`() =
        runBlocking {
            val dao = database.estudianteDao()
            dao.insertAll(
                listOf(
                    estudiante(codigo = "20200001", dni = "70000001"),
                    estudiante(codigo = "20200002", dni = "70000001"),
                ),
            )

            assertEquals(1, dao.contarTotal())
            assertEquals("20200002", dao.obtenerPorDni("70000001")?.codigo)
        }

    @Test
    fun `search matches name code dni and institutional email exactly`() =
        runBlocking {
            val dao = database.estudianteDao()
            val expected =
                estudiante(
                    codigo = "20201234",
                    dni = "70123456",
                    nombre = "Ana Pérez Quispe",
                )
            dao.insertAll(
                listOf(
                    expected,
                    estudiante(codigo = "20205678", dni = "70876543", nombre = "Luis Rojas Soto"),
                ),
            )

            listOf("Pérez", "20201234", "70123456", "20201234@unsch.edu.pe").forEach { filter ->
                assertEquals(listOf(expected), dao.buscarEstudiantes(filter).first())
            }
        }

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
}
