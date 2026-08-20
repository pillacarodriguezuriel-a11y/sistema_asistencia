# Guía de contribución

Este proyecto utiliza un Git Flow adaptado a sprints semanales. Todo cambio se
integra mediante Pull Request (PR); no se trabaja directamente sobre las ramas
protegidas.

## Ramas permanentes

### `main` — producción

Contiene únicamente incrementos revisados y aptos para distribuir. Cada merge a
`main` debe proceder de un PR de release desde `develop` o de un hotfix
excepcional.

En GitHub, el administrador debe configurar una ruleset para `main` con:

- prohibición de push directo, eliminación y force-push;
- PR obligatorio antes de integrar;
- al menos una aprobación y descarte de aprobaciones al agregar commits;
- conversaciones resueltas antes del merge;
- check obligatorio `Android build and tests` actualizado con la rama;
- historial lineal y merge mediante **Squash and merge**;
- restricción de bypass a administradores de release;
- etiquetas de versión siguiendo SemVer (`vMAJOR.MINOR.PATCH`).

### `develop` — integración

Acumula el incremento del sprint y es la rama base de todas las características.
Debe mantenerse compilable y con CI verde.

En GitHub, el administrador debe configurar una ruleset para `develop` con:

- prohibición de push directo, eliminación y force-push;
- PR obligatorio con al menos una aprobación;
- conversaciones resueltas;
- check obligatorio `Android build and tests`;
- **Squash and merge** para conservar un commit trazable por Issue.

La protección efectiva se configura en **Settings > Rules > Rulesets** del
repositorio GitHub. Las reglas de servidor no pueden imponerse únicamente desde
un archivo versionado.

## Ramas de trabajo

Una característica se crea desde `develop` con el patrón:

```text
feature/sprint-X-issue-Y-descripcion-corta
```

Ejemplos:

```text
feature/sprint-1-issue-2-ci-inicial
feature/sprint-1-issue-3-room
```

Flujo recomendado:

```bash
git switch develop
git pull --ff-only origin develop
git switch -c feature/sprint-1-issue-3-room
# trabajar y crear commits pequeños
git push -u origin feature/sprint-1-issue-3-room
```

El PR de una rama `feature/*` apunta a `develop`. Después del merge, la rama se
elimina. No se mezclan varios Issues sin una justificación explícita.

Para correcciones urgentes de producción se permite
`hotfix/issue-Y-descripcion`; se crea desde `main` y su resultado debe integrarse
tanto en `main` como en `develop`.

## Commits semánticos

Formato:

```text
tipo(alcance opcional): descripción imperativa breve
```

Tipos mínimos:

- `feat:` nueva capacidad funcional.
- `fix:` corrección de un defecto.
- `chore:` mantenimiento, configuración o dependencias.
- `docs:` documentación sin cambio funcional.

Tipos adicionales aceptados: `test:`, `refactor:`, `perf:`, `ci:` y `build:`.

Ejemplos:

```text
feat(scanner): registrar asistencia por DNI
fix(import): preservar ceros iniciales del DNI
ci: publicar APK debug como artefacto
docs: documentar contrato de columnas del padrón
```

La primera línea no debe terminar en punto y debe explicar qué cambia. Para un
cambio incompatible se usa `!` y un pie `BREAKING CHANGE:`. No se incluyen
contraseñas, llaves, datos personales ni archivos generados en los commits.

## Pull Requests

Antes de abrir un PR:

1. Actualizar la rama desde `develop` y resolver conflictos localmente.
2. Ejecutar `./gradlew testDebugUnitTest assembleDebug`.
3. Completar la plantilla, enlazar el Issue y declarar el rol responsable.
4. Mantener el PR pequeño, revisable y limitado a su criterio de aceptación.
5. Solicitar revisión al menos a un integrante distinto del autor.

El título del PR también sigue commits semánticos, por ejemplo:
`feat(import): implementar HU-01 (#3)`.

## Definition of Done para integración

- Código y documentación del Issue incluidos.
- Criterios de aceptación verificados y evidencia adjunta.
- Pruebas unitarias relevantes agregadas o actualizadas.
- `testDebugUnitTest` y `assembleDebug` aprobados en CI.
- Sin advertencias críticas, secretos ni datos personales versionados.
- Funcionalidad validada en modo avión cuando corresponda.
- Revisión aprobada y conversaciones resueltas.
- PR dirigido a la rama correcta y listo para squash merge.

## Releases y firma

`keystore.properties` y las llaves (`*.jks`, `*.keystore`) son locales y están
ignorados por Git. Para compilar release, copiar
`keystore.properties.example` a `keystore.properties`, completar valores reales
y mantener la llave fuera del repositorio. En CI, una futura tarea de release
deberá reconstruir ambos desde GitHub Actions Secrets; el CI inicial solo genera
APK Debug y no necesita secretos.

