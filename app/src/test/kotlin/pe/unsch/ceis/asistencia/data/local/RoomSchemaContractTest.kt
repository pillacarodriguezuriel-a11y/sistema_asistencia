package pe.unsch.ceis.asistencia.data.local

import java.io.File
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RoomSchemaContractTest {
    private val database = Json.parseToJsonElement(schemaFile().readText())
        .jsonObject
        .getValue("database")
        .jsonObject

    @Test
    fun `schema version one contains the three required tables`() {
        assertEquals("1", database.getValue("version").jsonPrimitive.content)
        assertEquals(
            setOf("estudiantes", "actividades", "asistencias"),
            entities().map { it.getValue("tableName").jsonPrimitive.content }.toSet(),
        )
    }

    @Test
    fun `student dni and attendance pair have unique indices`() {
        val dniIndex = entity("estudiantes").indices().single {
            it.columnNames() == listOf("dni")
        }
        val attendanceIndex = entity("asistencias").indices().single {
            it.columnNames() == listOf("actividadId", "estudianteCodigo")
        }

        assertTrue(dniIndex.getValue("unique").jsonPrimitive.content.toBoolean())
        assertTrue(attendanceIndex.getValue("unique").jsonPrimitive.content.toBoolean())
    }

    @Test
    fun `attendance foreign keys cascade on activity and student deletion`() {
        val foreignKeys = entity("asistencias")
            .getValue("foreignKeys")
            .jsonArray
            .map { it.jsonObject }

        assertEquals(setOf("actividades", "estudiantes"), foreignKeys.map {
            it.getValue("table").jsonPrimitive.content
        }.toSet())
        assertTrue(foreignKeys.all {
            it.getValue("onDelete").jsonPrimitive.content == "CASCADE"
        })
    }

    private fun schemaFile(): File = File(
        "schemas/pe.unsch.ceis.asistencia.data.local.AppDatabase/1.json",
    ).also { file ->
        check(file.isFile) { "No se encontró el esquema Room v1 en ${file.absolutePath}" }
    }

    private fun entities(): List<JsonObject> = database
        .getValue("entities")
        .jsonArray
        .map { it.jsonObject }

    private fun entity(tableName: String): JsonObject = entities().single {
        it.getValue("tableName").jsonPrimitive.content == tableName
    }

    private fun JsonObject.indices(): List<JsonObject> =
        (this["indices"] as? JsonArray).orEmpty().map { it.jsonObject }

    private fun JsonObject.columnNames(): List<String> = getValue("columnNames")
        .jsonArray
        .map { it.jsonPrimitive.content }
}
