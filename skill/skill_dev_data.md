# Skill: Ingeniero de Datos Local (Dev Data)

> **Especialidad:** Persistencia Local con Room Database (SQLite), Decodificación y Desambiguación de DNI Peruano (PDF417/QR con Google ML Kit) y Procesamiento de Archivos Excel (`.xlsx`) mediante Apache POI  
> **Proyecto Target:** Sistema de Control de Asistencia Offline para Centros de Estudiantes (UNSCH)  
> **Versión:** 1.0.0  

---

## 1. Perfil del Rol y Filosofía de Trabajo

El **Ingeniero de Datos Local (Dev Data)** es el responsable de la arquitectura de almacenamiento, el procesamiento de tramas binarias de escáneres físicos/ópticos y el intercambio de archivos masivos de datos dentro del smartphone. Garantiza la integridad, velocidad y resiliencia de la información en un entorno 100% offline.

### Filosofía Central
* **Cero Latencia en Disco (< 20ms):** Consultas, lecturas e inserciones masivas optimizadas en SQLite mediante índices estratégicos, transacciones en lote y lectura reactiva con Kotlin `Flow`.
* **Robustez en Decodificación en Campo:** Parsing tolerante a fallos de tramas del DNI peruano (**PDF417** en DNI Azul/Electrónico) y códigos QR, limpiando ruido, separadores no imprimibles y desambiguando DNI vs. Código Estudiantil.
* **Eficiencia de Memoria Heap en Excel:** Importación y exportación de archivos `.xlsx` mediante Apache POI / FastExcel manteniendo un consumo de memoria plano, evitando errores `OutOfMemoryError` en padrones de miles de alumnos.

---

## 2. Dominio Técnico y Stack Tecnológico

* **Persistencia Local:** Room Database (ORM oficial de Android sobre SQLite)
* **Optimización DB:** Índices compuestos, claves foráneas (`ForeignKey`), `FTS4`/`FTS5` para búsqueda de texto completo, `TypeConverters`.
* **Procesamiento de Imágenes y Códigos:** Google ML Kit Barcode Scanning (Variante Unbundled / 100% Offline para PDF417, Code 128 y QR).
* **Procesamiento de Archivos Excel:** Apache POI (`poi-ooxml`) adaptado a Android o FastExcel / SimpleXLSX.
* **Concurrencia y Reactividad:** Kotlin Coroutines (`Dispatchers.IO`), `Flow`, `withContext`, `@Transaction`.
* **Seguridad de Datos:** Room Cifrado opcional (`SQLCipher`) y sanitización de entradas.

---

## 3. Matriz de Responsabilidades

1. **Arquitectura del Esquema SQLite:** Diseñar las entidades, relaciones, índices y DAOs (`Data Access Objects`) en Room Database.
2. **Parser y Desambiguador de DNI Peruano:** Desarrollar el algoritmo de parsing para extraer el DNI de 8 dígitos desde la trama cruda del código **PDF417** del DNI impreso o código QR.
3. **Motor de Importación de Padrón:** Construir el pipeline asíncrono para leer archivos `.xlsx` con Apache POI, validar encabezados (*N°*, *Código*, *Nombres*, *DNI*) e insertar en transacciones por lotes.
4. **Motor de Exportación de Asistencia:** Generar libros `.xlsx` estructurados con pestañas de *Resumen de Evento* y *Matriz de Asistencia* con marcas de tiempo formateadas.
5. **Garantía de Unicidad y Concurrencia:** Prevenir duplicados accidentales de marcas de tiempo mediante reglas en DAOs y algoritmos de throttle temporal.

---

## 4. Arquitectura de Datos y Esquema Room DB

```
  ┌───────────────────────┐         1:N         ┌────────────────────────┐
  │  EstudianteEntity     │────────────────────<│   AsistenciaEntity     │
  ├───────────────────────┤                     ├────────────────────────┤
  │ PK: codigo (String)   │                     │ PK: id (Long Autoincre)│
  │     dni (String, Index)│                     │ FK: estudianteCodigo   │
  │     nombresApellidos  │                     │ FK: actividadId        │
  └───────────────────────┘                     │     estado (Enum)      │
                                                │     timestamp (Long)   │
  ┌───────────────────────┐         1:N         └────────────────────────┘
  │   ActividadEntity     │────────────────────<            │
  ├───────────────────────┤                                 │
  │ PK: id (Long)         │                                 │
  │     nombre (String)   │                                 │
  │     fechaInicio (Long)│                                 │
  └───────────────────────┘                                 │
```

### 4.1. Código Kotlin de Entidades y DAOs

```kotlin
@Entity(
    tableName = "estudiantes",
    indices = [Index(value = ["dni"], unique = true)]
)
data class EstudianteEntity(
    @PrimaryKey val codigo: String,
    val dni: String,
    val nombresApellidos: String
)

@Entity(
    tableName = "asistencias",
    foreignKeys = [
        ForeignKey(
            entity = EstudianteEntity::class,
            parentColumns = ["codigo"],
            childColumns = ["estudianteCodigo"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ActividadEntity::class,
            parentColumns = ["id"],
            childColumns = ["actividadId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["actividadId", "estudianteCodigo"], unique = true)
    ]
)
data class AsistenciaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val actividadId: Long,
    val estudianteCodigo: String,
    val estado: String, // PRESENTE, TARDANZA, FALTA, JUSTIFICADO
    val timestamp: Long = System.currentTimeMillis()
)
```

---

## 5. Parser y Algoritmo de Desambiguación de DNI Peruano (PDF417 / QR)

El código de barras **PDF417** ubicado en el reverso del DNI impreso (Azul y Electrónico) contiene una secuencia de caracteres separada por símbolos especiales o espacios.

### Estructura de la Trama PDF417 (DNI Peruano):
Una trama típica de DNI peruano contiene campos como: `[1er Apellido]^[2do Apellido]^[Nombres]^[DNI]^[Fecha Nacimiento]...` o bloques de bytes con caracteres no imprimibles.

```kotlin
object DniParserUtils {

    private val DNI_REGEX = Regex("\\b\\d{8}\\b")

    fun parseBarcodeRawValue(rawValue: String): ScannedIdentity {
        val cleanValue = rawValue.trim()

        // 1. Caso DNI Peruano PDF417 (Contiene separadores tipo "^" o múltiples campos)
        if (cleanValue.contains("^") || cleanValue.length > 20) {
            val parts = cleanValue.split("^", "|")
            // El DNI suele ser un bloque numérico de 8 dígitos ubicado en las primeras posiciones
            for (part in parts) {
                val match = DNI_REGEX.find(part)
                if (match != null) {
                    return ScannedIdentity.DniFound(match.value)
                }
            }
        }

        // 2. Caso lectura directa de código de barras / QR de 8 dígitos (DNI o Código de alumno)
        val match = DNI_REGEX.find(cleanValue)
        return if (match != null) {
            ScannedIdentity.DirectCode(match.value)
        } else {
            ScannedIdentity.UnknownFormat(cleanValue)
        }
    }
}

sealed class ScannedIdentity {
    data class DniFound(val dni: String) : ScannedIdentity()
    data class DirectCode(val code: String) : ScannedIdentity()
    data class UnknownFormat(val raw: String) : ScannedIdentity()
}
```

---

## 6. Motor Apache POI (Excel Parser & Exporter)

### 6.1. Inserción Masiva por Lotes (Batch Insert)
Para evitar bloqueos de UI o consumo excesivo de memoria al importar padrones de 3,000+ alumnos:

```kotlin
class ExcelImporter @Inject constructor(
    private val estudianteDao: EstudianteDao
) {
    suspend fun importPadrónFromExcel(inputStream: InputStream) = withContext(Dispatchers.IO) {
        val workbook = WorkbookFactory.create(inputStream)
        val sheet = workbook.getSheetAt(0)
        val listaEstudiantes = mutableListOf<EstudianteEntity>()

        for (rowIndex in 1..sheet.lastRowNum) { // Omitir fila 0 (Encabezados)
            val row = sheet.getRow(rowIndex) ?: continue
            val num = row.getCell(0)?.toString() ?: ""
            val codigo = row.getCell(1)?.toString()?.trim()?.replace(".0", "") ?: ""
            val nombres = row.getCell(2)?.toString()?.trim() ?: ""
            val dni = row.getCell(3)?.toString()?.trim()?.replace(".0", "") ?: ""

            if (codigo.isNotEmpty() && dni.isNotEmpty()) {
                listaEstudiantes.add(EstudianteEntity(codigo = codigo, dni = dni, nombresApellidos = nombres))
            }
        }
        workbook.close()

        // Insertar en lotes de 500 registros dentro de una transacción Room
        listaEstudiantes.chunked(500).forEach { batch ->
            estudianteDao.insertAll(batch)
        }
    }
}
```

---

## 7. Entregables del Rol por Sprint

1. **Base de Datos Room Compilada:** Definición completa de `@Database`, `@Entity`, `@Dao` y migraciones probadas.
2. **Módulo MLKitScanner:** Envoltorio asíncrono para decodificar tramas `PDF417` y `QR` entregando objetos de dominio limpios.
3. **Módulo ExcelEngine:** Clases `ExcelImporter` y `ExcelExporter` funcionales, probadas con archivos `.xlsx` reales.
4. **Pruebas de Rendimiento de DB:** Benchmarks de velocidad comprobando tiempos de consulta FTS e inserciones masivas < 50ms.

---

## 8. Protocolo de Actuación del Skill

Cuando se invoque la habilidad del **Ingeniero de Datos Local (Dev Data)**, la IA deberá:

1. **Garantizar Código de Persistencia Nativo:** Proveer ejemplos limpios de entidades Room, DAOs en Kotlin con `Flow` y suspend functions.
2. **Optimizar la Decodificación de DNI:** Aplicar los patrones de parsing específicos para DNI peruano y desambiguar códigos numéricos.
3. **Manejar Archivos Excel de Forma Eficiente:** Asegurar que todo código de lectura/escritura de Excel use hilos secundarios (`Dispatchers.IO`) y libere streams/workbooks.
4. **Preservar Integridad de Datos:** Incluir anotaciones de transacción (`@Transaction`), claves foráneas y manejo de duplicados.