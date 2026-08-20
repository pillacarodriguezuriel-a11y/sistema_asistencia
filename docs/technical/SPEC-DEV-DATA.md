# Especificación de implementación — Dev Data

## Alcance

Dev Data implementará persistencia Room, importación `.xlsx`/`.csv`, contrato
de decodificación de DNI, registro atómico de asistencia y exportación `.xlsx`.
Todas las operaciones de disco y análisis se ejecutan fuera del hilo principal.

## Modelo mínimo de datos

### Estudiante

| Campo | Tipo | Regla |
|---|---|---|
| `codigo` | `String` | PK, no vacío, conservar ceros |
| `dni` | `String` | único, regex `^[0-9]{8}$` |
| `apellidoNombre` | `String` | no vacío |
| `correoInstitucional` | `String` | no vacío, normalizado a minúsculas |

Room debe declarar índice único para `dni`. El código es la clave primaria. La
actualización por importación debe resolver simultáneamente ambas identidades;
si código y DNI apuntan a filas distintas, devuelve conflicto y hace rollback.

### Actividad y asistencia

`Actividad` debe exponer como mínimo `id`, `nombre` y `fechaInicio`. La entidad
`Asistencia` debe incluir `id`, `actividadId`, `estudianteCodigo`, `estado` y
`timestampEpochMillis`, con claves foráneas e índice único compuesto
`(actividadId, estudianteCodigo)`. Los estados de dominio son `PRESENTE`,
`TARDANZA`, `FALTA` y `JUSTIFICADO`.

La alternativa `Invitado/No empadronado` debe persistirse en una entidad o
agregado separado del padrón oficial y referenciarse desde la asistencia; nunca
debe crear un `Estudiante` oficial de manera implícita. El diseño final de esa
entidad pertenece al Issue de persistencia y debe conservar la misma regla de
unicidad por actividad.

## Contrato de importación

```kotlin
data class StudentImportRow(
    val rowNumber: Int,
    val apellidoNombre: String,
    val codigo: String,
    val dni: String,
    val correoInstitucional: String,
)

data class ImportSummary(
    val totalRead: Int,
    val inserted: Int,
    val updated: Int,
)

sealed interface ImportResult {
    data class Success(val summary: ImportSummary) : ImportResult
    data class InvalidHeaders(
        val expected: List<String>,
        val found: List<String>,
    ) : ImportResult
    data class InvalidRows(val errors: List<RowError>) : ImportResult
    data class IdentityConflict(val rowNumber: Int) : ImportResult
    data class Failure(val reason: String) : ImportResult
}
```

Encabezados canónicos, en orden:

```kotlin
val REQUIRED_HEADERS = listOf(
    "Apellido y nombre",
    "Código de estudiante",
    "DNI",
    "correo institucional",
)
```

Pipeline obligatorio:

1. Resolver el formato por extensión/MIME y abrir el `InputStream` desde el
   `ContentResolver`.
2. Leer la primera hoja en `.xlsx` con Apache POI, o CSV UTF-8 con un parser que
   respete campos entre comillas.
3. Cerrar `InputStream` y workbook mediante `use` incluso ante excepción.
4. Validar encabezados antes de crear filas de dominio.
5. Leer identificadores como texto. En celdas numéricas de XLSX usar un
   formateador que no produzca el sufijo `.0` ni notación científica.
6. Validar todo el archivo, incluyendo duplicados y conflictos con Room.
7. Aplicar la última fila válida cuando un mismo código/DNI se repite dentro
   del archivo.
8. Ejecutar el upsert lógico completo dentro de `withTransaction`.
9. Devolver un resultado tipado; no filtrar silenciosamente filas inválidas.

Un `InvalidHeaders`, `InvalidRows`, `IdentityConflict` o excepción no deja
cambios parciales.

## Contrato de escaneo y asistencia

El analizador entrega la trama; el parser devuelve un resultado tipado:

```kotlin
sealed interface IdentityParseResult {
    data class Dni(val value: String) : IdentityParseResult
    data object Ambiguous : IdentityParseResult
    data object Unsupported : IdentityParseResult
}

sealed interface MarkAttendanceResult {
    data class Registered(val student: Student) : MarkAttendanceResult
    data class AlreadyRegistered(val student: Student) : MarkAttendanceResult
    data class StudentNotFound(val dni: String) : MarkAttendanceResult
    data object InvalidActivity : MarkAttendanceResult
}
```

- No se selecciona automáticamente el primer bloque de ocho dígitos si la
  trama contiene varios candidatos indistinguibles.
- `markPresent(activityId, dni)` consulta y registra en una transacción.
- Una violación del índice único se mapea a `AlreadyRegistered`, no a error
  genérico.
- El timestamp se inyecta mediante un `Clock` comprobable y se persiste una vez.

## Contrato de exportación

La consulta de exportación produce filas inmutables con los siete campos
definidos en HU-03. Apache POI crea la hoja `Asistencia`, congela el encabezado,
escribe DNI/código como texto y utiliza un libro/estrategia compatible con el
volumen esperado. El exporter debe:

- recibir un `OutputStream` o archivo privado controlado por la aplicación;
- ordenar de forma determinista antes de escribir;
- cerrar workbook y stream mediante `use`;
- borrar el temporal si falla la escritura;
- informar progreso y resultado sin exponer excepciones técnicas a la UI.

## Pruebas mínimas del Issue Dev Data

- Mapeo exacto de los cuatro encabezados para XLSX y CSV.
- Preservación de ceros iniciales y rechazo de DNI inválido.
- Rollback por encabezado/fila/conflicto de identidad.
- Conteo correcto de insertados y actualizados, incluidos duplicados internos.
- Índice único de asistencia bajo dos inserciones concurrentes.
- Parser con cero, uno y varios candidatos de ocho dígitos.
- Exportación con los siete encabezados, estados, orden y timestamps definidos.
- Ejecución completa en modo avión.

## Fuera de alcance

Sincronización cloud, autenticación remota, analítica, modificación automática
del padrón al registrar invitados y dependencia de servicios web.
