# Sprint 1 — Issue 5: Sistema de diseño Material 3

## Ficha

- Rol responsable: UI/UX
- Rama: `feature/sprint-1-issue-5-design-system`
- Rama base temporal: `feature/sprint-1-issue-4-clean-architecture`
- Prioridad: P0
- Estado: Implementado y validado localmente; pendiente de PR

## Entregables

- Paleta institucional verde/gris/blanco para temas claro y oscuro.
- Tokens semánticos oficiales para los cuatro estados.
- Escala tipográfica outdoor y superficies de alto contraste.
- `ScannerOverlayBox`, `StudentAttendanceCard`, `QuickStatusChipGroup` y
  `PrimaryActionButton`.
- Feedback háptico/sonoro desacoplado de Compose.
- Previews claro/oscuro en cada componente.
- Guía formal en `docs/design/DESIGN_SYSTEM.md`.

## Decisiones de accesibilidad

- Targets interactivos: mínimo 48 dp; botón primario: 56 dp.
- Estados comunicados con texto, semántica, borde y color.
- Los colores semánticos oficiales se usan como acento/tinte, manteniendo texto
  en `onSurface` para contraste AAA.
- Dynamic Color permanece desactivado por defecto para preservar la identidad.

## Dependencia entre ramas

Esta rama incluye temporalmente los Issues 3 y 4. Después de integrar esos PRs,
debe rebasarse sobre `develop` antes de su PR definitivo.

## Verificación

```text
./gradlew verifyDomainPurity
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

Además, cada componente debe compilar con dos anotaciones `@Preview`, una clara
y una oscura.

Validación local del 20 de agosto de 2026:

- `verifyDomainPurity`: aprobado.
- `testDebugUnitTest`: 4 pruebas, 0 fallos.
- `assembleDebug`: aprobado.
- Previews: 2 por cada componente solicitado.
- Contraste primario: 7.87:1 claro y 8.48:1 oscuro.
- Contraste de superficie: 18.55:1 claro y 17.01:1 oscuro.
- Resultado: `BUILD SUCCESSFUL`, 52 tareas verificadas.
