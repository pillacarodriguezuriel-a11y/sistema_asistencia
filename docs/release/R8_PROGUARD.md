# R8, ProGuard y control de tamaño release

## Objetivo

La variante `release` usa el optimizador R8, ofuscación y reducción de recursos.
La meta de distribución es estrictamente menor a 15 MiB por APK para facilitar
su envío fuera de Google Play.

```kotlin
release {
    isMinifyEnabled = true
    isShrinkResources = true
    proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro",
    )
}
```

## Verificación reproducible

```text
./gradlew assembleRelease
```

`assembleRelease` ejecuta al finalizar `verifyReleaseApkSize`, que:

1. localiza todas las APK generadas en `app/build/outputs/apk/release`;
2. falla si alguna pesa 15 MiB o más;
3. exige `mapping.txt` para reconstruir trazas ofuscadas;
4. exige `configuration.txt` para auditar la configuración efectiva de R8;
5. verifica que sigan declaradas las reglas críticas de Room, Hilt, POI,
   ML Kit, Compose y coroutines;
6. inspecciona `mapping.txt` para confirmar que la Activity Hilt, el ViewModel y
   su Factory mantienen sus nombres requeridos.

Sin `keystore.properties`, Gradle genera `app-release-unsigned.apk`. Esto es
intencional para CI: valida exactamente el mismo código minificado sin requerir
ni exponer secretos. La firma institucional se aplica únicamente en un entorno
de release autorizado.

## Política de reglas

- Las reglas conservan tipos concretos que pueden resolverse por reflexión o
  generación de código; no se deshabilitan globalmente shrink, optimize ni
  obfuscate.
- Apache POI conserva únicamente la API de libro/hoja/fila/celda y sus modelos
  XSSF principales. No se mantiene `org.apache.poi.**` completo.
- Room, Apache POI y ML Kit todavía no forman parte de las dependencias del
  módulo. Las reglas quedan preparadas y comenzarán a aplicarse cuando cada
  librería sea incorporada en sus Issues de implementación.
- `mapping.txt` es un artefacto sensible de soporte: debe almacenarse junto a
  cada release publicada, pero no se versiona en Git.

## Referencias técnicas

- [Android: reglas keep de R8](https://developer.android.com/topic/performance/app-optimization/add-keep-rules)
- [Android: opciones globales y atributos](https://developer.android.com/topic/performance/app-optimization/global-options)
- [ML Kit: Barcode Scanning en Android](https://developers.google.com/ml-kit/vision/barcode-scanning/android)
- [Apache POI: guía XSSF para hojas y celdas](https://poi.apache.org/components/spreadsheet/quick-guide.html)
