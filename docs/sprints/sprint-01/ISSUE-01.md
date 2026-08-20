# Sprint 1 — Issue 1: Redacción y refinamiento de HU críticas

## Ficha del Issue

- Rol responsable: PO/SM
- Sprint: 1
- Prioridad: P0
- Valor de negocio: Muy alto
- Estimación: 3 SP
- Estado del entregable: Ejecutado; pendiente de cierre formal del DoD

## Objetivo

Registrar HU-01, HU-02 y HU-03 con criterios verificables y entregar contratos
técnicos suficientes para que Dev Data y Dev Mobile implementen los siguientes
Issues manteniendo una arquitectura 100 % offline.

## Entregables

- [HU-01 — Importación del padrón](../../product-backlog/HU-01.md)
- [HU-02 — Escaneo DNI/QR](../../product-backlog/HU-02.md)
- [HU-03 — Exportación Excel](../../product-backlog/HU-03.md)
- [Especificación Dev Data](../../technical/SPEC-DEV-DATA.md)
- [Especificación Dev Mobile](../../technical/SPEC-DEV-MOBILE.md)

## Acta de decisiones

### DEC-001 — Contrato estricto de columnas

La fuente de verdad es el acta del Issue 1. El archivo de entrada contiene
exactamente `Apellido y nombre`, `Código de estudiante`, `DNI` y
`correo institucional`, en ese orden. Esta decisión sustituye el ejemplo
anterior del contexto Dev Data (`N°`, `Código`, `Nombres`, `DNI`).

### DEC-002 — Transaccionalidad de importación

Se adopta validación completa antes de escritura y una sola operación lógica
transaccional. Un encabezado, fila o cruce de identidad inválido deja el padrón
sin cambios. Así, el mensaje de éxito siempre describe una importación íntegra.

### DEC-003 — Separación tecnológica por formato

Apache POI procesa `.xlsx`. El `.csv` usa un lector CSV compatible con campos
entrecomillados; no se simula CSV mediante separación manual por comas. Ambos
adaptadores entregan el mismo modelo de fila al validador de dominio.

### DEC-004 — Unicidad de asistencia en persistencia

El bloqueo por debounce mejora la experiencia, pero Room impone la unicidad de
una asistencia por estudiante y actividad para cubrir relecturas y carreras.

### DEC-005 — Datos personales y operación offline

DNI y correo solo se almacenan localmente y aparecen en el archivo exportado
por acción explícita del usuario. Ningún flujo requiere red, analítica o carga a
la nube.

## Dependencias para los siguientes Issues

- Debe definirse o implementarse el agregado `Actividad` antes de aceptar
  HU-02/HU-03 de extremo a extremo.
- DevOps debe fijar versiones compatibles de Room, Apache POI, CameraX, ML Kit,
  Compose y Hilt, además del `FileProvider`.
- UI/UX debe suministrar estados visuales y accesibles para progreso, éxito,
  duplicado, no encontrado, error y permiso denegado.
- QA debe preparar fixtures válidos e inválidos `.xlsx`, `.csv`, PDF417 y QR.

## Checklist DoR de las HU

- [x] Título, rol y beneficio definidos.
- [x] Criterios Gherkin medibles.
- [x] Reglas de datos y errores especificadas.
- [x] Dependencias y responsables identificados.
- [x] Estimación inicial incluida.
- [ ] Estimación ratificada por el equipo en Sprint Planning.

## Checklist DoD del Issue 1

- [x] Historias registradas en la estructura del proyecto.
- [x] Contratos técnicos documentados.
- [x] Trazabilidad entre historias, reglas y roles.
- [ ] Repositorio Git inicializado y cambios integrados en `develop` mediante PR.
- [ ] Revisión y aceptación formal del PO/SM.

El Issue no debe marcarse cerrado hasta completar los dos puntos pendientes; el
directorio recibido no contiene metadatos Git, por lo que no es posible aportar
evidencia de PR o integración a `develop` desde este entregable.
