# Sistema de asistencia offline

Aplicación Android para gestionar padrones y asistencias de los Centros de
Estudiantes de la UNSCH sin depender de conexión a internet.

## Documentación del producto

- [Product Backlog](docs/product-backlog/README.md)
- [Issue 1 del Sprint 1](docs/sprints/sprint-01/ISSUE-01.md)
- [Issue 2 del Sprint 1](docs/sprints/sprint-01/ISSUE-02.md)
- [Issue 3 del Sprint 1](docs/sprints/sprint-01/ISSUE-03.md)
- [Issue 4 del Sprint 1](docs/sprints/sprint-01/ISSUE-04.md)
- [Contrato Clean Architecture](docs/architecture/CLEAN_ARCHITECTURE.md)
- [Issue 5 del Sprint 1](docs/sprints/sprint-01/ISSUE-05.md)
- [Issue 7 del Sprint 1](docs/sprints/sprint-01/ISSUE-07.md)
- [Guía de estilos Material 3](docs/design/DESIGN_SYSTEM.md)
- [Contrato de inyección de dependencias](docs/architecture/DEPENDENCY_INJECTION.md)
- [Contrato técnico para Dev Data](docs/technical/SPEC-DEV-DATA.md)
- [Contrato técnico para Dev Mobile](docs/technical/SPEC-DEV-MOBILE.md)

## Regla de datos vigente

El padrón estudiantil de entrada tiene exactamente cuatro columnas, en este
orden:

1. `Apellido y nombre`
2. `Código de estudiante`
3. `DNI`
4. `correo institucional`

Esta definición reemplaza cualquier estructura anterior mencionada en los
archivos de contexto de roles.
