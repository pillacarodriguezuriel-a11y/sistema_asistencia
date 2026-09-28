# Sprint 2 — Issue 14: Estados y confirmación de persistencia

## Ficha

- Rol responsable: UI/UX
- Estado: Implementado y validado por compilación Compose; pendiente de PR
- Prioridad: P1

## Componentes

- `PersistenceUiState`: contrato inmutable con estados `Idle`, `Saving`,
  `Success` y `Error`.
- `PersistenceSnackbar`: feedback accesible para guardado y colisiones. Los
  resultados terminales se ocultan automáticamente a los 1 800 ms, por debajo
  del límite de dos segundos; `Saving` permanece visible hasta cambiar de
  estado.
- `ConfirmationDialog`: confirmación explícita para eliminaciones en cascada,
  con botones de altura mínima de 48 dp y acción destructiva diferenciada por
  el color semántico de error.
- `EmptyStateCard`: ilustración dibujada con Compose y mensajes reutilizables
  para padrones o actividades vacías.

Todos los componentes incluyen previsualización en tema claro y oscuro. El
snackbar declara una región viva accesible para que los lectores de pantalla
anuncien el resultado sin interrumpir el flujo del operador.

## Uso esperado

La pantalla o ViewModel conserva el estado. Tras recibir `Success` o `Error`,
`PersistenceSnackbar` invoca `onDismiss`, que debe restablecer el estado a
`Idle`. Antes de ejecutar una eliminación, la UI presenta
`ConfirmationDialog`; la operación solo se inicia desde `onConfirm`.
