# Sprint 1 — Issue 2: Repositorio, ramas y CI inicial

## Ficha

- Rol responsable: DevOps
- Prioridad: P0
- Estado local: Implementado y verificado
- Estado GitHub: Pendiente de crear/vincular el remoto y aplicar rulesets

## Entregables

- `CONTRIBUTING.md`: Git Flow, ramas protegidas, commits y DoD.
- `.gitignore`: exclusiones de Android, Gradle, IDE, artefactos y secretos.
- `.github/workflows/android_ci.yml`: pruebas, APK Debug y artefacto.
- `.github/PULL_REQUEST_TEMPLATE.md`: plantilla obligatoria de revisión.
- Gradle Kotlin DSL raíz y módulo `:app` con API 35 y Java 17.
- Gradle Wrapper 8.11.1 y prueba unitaria de humo.
- Firma release opcional mediante `keystore.properties` ignorado.

## Decisiones técnicas

- AGP 8.9.2 soporta API 35 y utiliza Gradle 8.11.1/JDK 17 como combinación
  compatible.
- Kotlin 2.1.21 se mantiene dentro de la línea compatible con AGP 8.9.
- El CI inicial genera únicamente APK Debug y, por tanto, no consume secretos.
- Release queda sin firma si falta cualquier propiedad o el archivo de llave;
  nunca se aplican credenciales por defecto.
- El nombre estable del check requerido en las reglas es
  `Android build and tests`.

## Validación requerida

```text
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

El segundo comando debe producir
`app/build/outputs/apk/debug/app-debug.apk`, la misma ruta publicada por CI.

## Acciones que requieren administración GitHub

1. Crear el repositorio remoto y añadirlo como `origin`.
2. Publicar `main` y `develop`.
3. Aplicar las rulesets descritas en `CONTRIBUTING.md`.
4. Marcar `develop` como rama predeterminada durante el desarrollo si así lo
   acuerda el equipo.
5. Verificar que el check `Android build and tests` sea obligatorio.

