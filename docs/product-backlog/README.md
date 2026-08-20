# Product Backlog

## Sprint 1

| ID | Historia | Prioridad | Valor de negocio | Estimación inicial | Dependencias | Estado |
|---|---|---:|---|---:|---|---|
| [HU-01](HU-01.md) | Carga e importación de padrón estudiantil | P0 | Muy alto | 8 SP | Selector de archivos; Room; motor XLSX/CSV | Ready |
| [HU-02](HU-02.md) | Toma de asistencia mediante DNI/QR | P0 | Muy alto | 13 SP | Actividad activa; padrón; CameraX; decodificador PDF417/QR | Ready con dependencia |
| [HU-03](HU-03.md) | Exportación de asistencia a Excel | P1 | Alto | 8 SP | Actividad con asistencias; Room; Apache POI; FileProvider | Ready con dependencia |

Las estimaciones son una referencia para el Sprint Planning y deben ser
ratificadas por Dev Data y Dev Mobile.

## Orden recomendado de implementación

1. HU-01: crea el padrón local que consume el escáner.
2. HU-02: registra asistencias sobre una actividad y el padrón existente.
3. HU-03: exporta el resultado persistido por HU-02.

## Definition of Ready aplicada

- Las tres historias usan la fórmula `Como / Quiero / Para`.
- Sus criterios son observables y están redactados en Gherkin.
- Cada historia identifica dependencias, reglas y dueño técnico principal.
- Los contratos de [Dev Data](../technical/SPEC-DEV-DATA.md) y
  [Dev Mobile](../technical/SPEC-DEV-MOBILE.md) fijan los límites entre capas.

## Decisión de producto DEC-001: encabezados del padrón

El acta del Issue 1 es la fuente de verdad y reemplaza los ejemplos antiguos de
los contextos de rol. La fila 1 del archivo debe contener exactamente, de
izquierda a derecha:

`Apellido y nombre` | `Código de estudiante` | `DNI` | `correo institucional`

No se admite la columna `N°`, no se admiten columnas adicionales y no se
aceptan alias como `Código`, `Nombres`, `Apellidos y Nombres` o `Correo`.
Para evitar rechazos por detalles invisibles, se permite retirar el BOM UTF-8
del primer encabezado y espacios exteriores; después de esa normalización, la
comparación es exacta y sensible a mayúsculas/minúsculas.
