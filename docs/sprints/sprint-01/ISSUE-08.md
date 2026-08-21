# Sprint 1 — Issue 8: Reglas base ProGuard/R8

## Ficha

- Rol responsable: DevOps & Release
- Rama: `feature/sprint-1-issue-8-proguard-r8`
- Rama base temporal: `feature/sprint-1-issue-7-global-hilt`
- Prioridad: P0
- Estado: Implementado y validado localmente; pendiente de PR

## Entregables

- Reglas R8 para Room, Hilt, Apache POI, ML Kit Barcode Scanning, Compose y
  coroutines.
- Preservación de anotaciones, firmas, clases internas y líneas necesarias para
  reflexión y reconstrucción de trazas.
- Variante release con minificación, ofuscación y resource shrinking activos.
- Verificador Gradle automático con límite estricto menor a 15 MiB.
- Comprobación de `mapping.txt`, `configuration.txt` y cobertura de reglas.
- Validación release incorporada al workflow Android CI.

## Dependencia entre ramas

Esta rama está apilada temporalmente sobre el Issue 7 porque los Issues previos
aún no están consolidados en `develop`. Debe rebasarse sobre `develop` después
de integrar sus ramas base y antes del PR definitivo.

## Verificación

```text
./gradlew testDebugUnitTest
./gradlew assembleRelease
```

Validación local del 21 de agosto de 2026:

- `testDebugUnitTest`: 5 pruebas, 0 fallos, 0 errores, 0 omitidas.
- `assembleRelease`: aprobado con R8 y reducción de recursos activos.
- `verifyReleaseApkSize`: aprobado.
- APK: `app-release-unsigned.apk`, 1 710 189 bytes (1.63 MiB).
- Meta: menor a 15 MiB; margen disponible: 13.37 MiB.
- SHA-256: `9D39E0076E4816053AEB68704E3F57880D495EE5E4A441A2D7A22A4F99DB99C6`.
- `mapping.txt`, `configuration.txt`, `seeds.txt`, `usage.txt` y
  `resources.txt`: generados.
- `Hilt_MainActivity`, `BaseViewModel` y `BaseViewModel_Factory`: nombres
  preservados y confirmados en `mapping.txt`.
- Resultado: `BUILD SUCCESSFUL`.
