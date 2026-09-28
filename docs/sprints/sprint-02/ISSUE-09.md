# Sprint 2 — Issue 9: Criterios de persistencia local

## Ficha

- Rol responsable: PO/SM
- Estado: Criterios documentados y automatizados; ejecución en dispositivo pendiente
- Prioridad: P0

## Historia técnica

**Como** directivo del Centro de Estudiantes  
**Quiero** que el padrón, las actividades y las asistencias se almacenen con
reglas de integridad verificables  
**Para** operar sin conexión sin duplicar ni perder información oficial.

## Criterios de aceptación

### Escenario 1: Código de estudiante único

- **Dado** un estudiante almacenado con un código determinado
- **Cuando** se intenta insertar otro estudiante con el mismo código
- **Entonces** SQLite impide que existan dos filas con ese código y la
  transacción no deja datos parciales.

### Escenario 2: DNI único

- **Dado** un estudiante almacenado con un DNI determinado
- **Cuando** se intenta insertar otro estudiante con el mismo DNI
- **Entonces** el índice único impide que existan dos filas con ese documento.

### Escenario 3: Prevención de doble marcado

- **Dado** un registro para un estudiante y una actividad
- **Cuando** se intenta registrar nuevamente la misma combinación
  `actividadId + estudianteCodigo`
- **Entonces** el índice compuesto único rechaza el duplicado y se conserva un
  solo registro.

### Escenario 4: Eliminación en cascada

- **Dado** una actividad con asistencias relacionadas
- **Cuando** se elimina la actividad
- **Entonces** SQLite elimina todas sus asistencias en la misma transacción
  mediante `ON DELETE CASCADE`.

### Escenario 5: Escritura offline atómica y rápida

- **Dado** el dispositivo sin conectividad y la base inicializada
- **Cuando** se realiza una escritura individual o por lote configurado
- **Entonces** la operación se completa íntegramente o se revierte íntegramente
  y su latencia P95, después del calentamiento, es menor a 50 ms en el
  dispositivo Android de referencia.

## Evidencias exigidas

- Esquema Room v1 exportado y versionado.
- Pruebas de instrumentación para unicidad, índice compuesto, cascada, JOIN y
  rollback transaccional.
- Prueba de rendimiento en SQLite real, sin red, con cinco escrituras de
  calentamiento y treinta mediciones; el percentil 95 debe ser `< 50 ms`.
- No se acepta `fallbackToDestructiveMigration()`.

## Definition of Done

- Compilación Debug y Release correcta.
- Consultas verificadas por el compilador Room/KSP.
- Pruebas unitarias aprobadas y APK de pruebas instrumentadas compilada.
- Pruebas instrumentadas ejecutadas en dispositivo/emulador antes del merge.
- PR revisado e integrado en `develop`.

## Estado de validación

El contrato del esquema se valida en JVM y la suite Android está compilada. La
ejecución de la prueba de latencia y de las pruebas SQLite instrumentadas queda
pendiente porque el host no tiene dispositivo ni emulador conectado.
