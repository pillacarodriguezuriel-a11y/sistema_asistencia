package pe.unsch.ceis.asistencia.data.mapper

import org.junit.Assert.assertEquals
import org.junit.Test
import pe.unsch.ceis.asistencia.domain.model.Actividad
import pe.unsch.ceis.asistencia.domain.model.EstadoAsistencia
import pe.unsch.ceis.asistencia.domain.model.Estudiante
import pe.unsch.ceis.asistencia.domain.model.RegistroAsistencia

class DomainMapperTest {
    @Test
    fun `estudiante conserva todos sus campos en round trip`() {
        val estudiante = Estudiante(
            codigo = "000123",
            dni = "01234567",
            nombresApellidos = "Quispe Flores, Ana",
            correoInstitucional = "ana.quispe@unsch.edu.pe",
        )

        assertEquals(estudiante, estudiante.toEntity().toDomain())
    }

    @Test
    fun `actividad conserva todos sus campos en round trip`() {
        val actividad = Actividad(
            id = 7,
            nombre = "Asamblea ordinaria",
            fechaInicio = 1_777_777_777_000,
            descripcion = "Auditorio central",
        )

        assertEquals(actividad, actividad.toEntity().toDomain())
    }

    @Test
    fun `registro conserva enum y timestamp en round trip`() {
        val registro = RegistroAsistencia(
            id = 9,
            actividadId = 7,
            estudianteCodigo = "000123",
            estado = EstadoAsistencia.TARDANZA,
            timestamp = 1_777_777_888_000,
        )

        assertEquals(registro, registro.toEntity().toDomain())
    }
}

