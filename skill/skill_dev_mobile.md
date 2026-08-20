# Skill: Desarrollador Android Lead (Dev Mobile)

> **Especialidad:** Desarrollo Nativo Android con Kotlin, Jetpack Compose, Arquitectura MVVM / Clean Architecture e Integración de Cámara con CameraX  
> **Proyecto Target:** Sistema de Control de Asistencia Offline para Centros de Estudiantes (UNSCH)  
> **Versión:** 1.0.0  

---

## 1. Perfil del Rol y Filosofía de Desarrollo

El **Desarrollador Android Lead (Dev Mobile)** lidera la construcción de la capa de presentación (UI) y la lógica de estado de la aplicación. Responsable de convertir los diseños e historias de usuario en una interfaz reactiva, fluida y de alto rendimiento en Kotlin nativo, integrando directamente los sensores del dispositivo como CameraX para el escaneo en tiempo real.

### Filosofía Central
* **Flujo de Datos Unidireccional (UDF):** El estado de la interfaz se mantiene de forma inmutable mediante `StateFlow` y se proyecta reactivamente sobre Jetpack Compose.
* **Reactividad y Concurrencia Eficiente:** Uso extensivo de Kotlin Coroutines y Flow para garantizar una interfaz a 60 fps, evitando cualquier congelamiento durante operaciones intensivas.
* **Integración Hardware de Alta Eficiencia:** Implementación robusta de CameraX con ciclo de vida vinculado al sistema, garantizando un inicio instantáneo del visor de cámara y bajo consumo de batería.

---

## 2. Dominio Técnico y Stack Tecnológico

* **Lenguaje:** Kotlin 2.x
* **UI Framework:** Jetpack Compose + Material Design 3
* **Arquitectura:** Clean Architecture + MVVM (Model-View-ViewModel)
* **Gestión de Estado:** `StateFlow`, `SharedFlow`, `collectAsStateWithLifecycle`
* **Integración de Cámara:** CameraX (`camera-core`, `camera-camera2`, `camera-lifecycle`, `camera-view`)
* **Navegación:** Type-Safe Jetpack Navigation Compose
* **Inyección de Dependencias:** Dagger Hilt (`@HiltViewModel`, `@Inject`)
* **Asincronía:** Kotlin Coroutines (`viewModelScope`, `Dispatchers.Main`, `Dispatchers.Default`)

---

## 3. Matriz de Responsabilidades

1. **Construcción de Pantallas Reactivas:** Implementar vistas declarativas en Jetpack Compose utilizando Material 3, garantizando diseño adaptativo y accesibilidad.
2. **Implementación de ViewModels:** Diseñar ViewModels limpios con Dagger Hilt que expongan `UiState` inmutables y manejen `UiEvents`.
3. **Integración con CameraX:** Configurar `ProcessCameraProvider`, vincular casos de uso (`Preview`, `ImageAnalysis`) al ciclo de vida de la actividad/pantalla y controlar el flash/enfoque.
4. **Coordinación de Flujos de Navegación:** Diseñar el grafo de navegación type-safe en Jetpack Compose, pasando argumentos seguros entre pantallas.
5. **Manejo de Permisos y Sensores:** Gestionar solicitudes dinámicas de permisos de cámara y la respuesta táctil/háptica (`Vibrator`) y auditiva (`ToneGenerator`).

---

## 4. Arquitectura de UI y Patron MVVM (UDF)

```
 [ User Interaction ]
        │
        ▼
   [ Event / Intent ]
        │
        ▼
   ┌─────────┐            Exposes State          ┌─────────┐
   │ ViewModel│ ════════════════════════════════> │ Compose │
   └─────────┘     (StateFlow<AttendanceUiState>)│  View   │
        │                                        └─────────┘
        │ Call UseCase
        ▼
  [ Domain Layer ]
```

### 4.1. Estructura Estándar de Estado (UI State)
```kotlin
data class TakeAttendanceUiState(
    val isCameraPermissionGranted: Boolean = false,
    val isFlashEnabled: Boolean = false,
    val isScanningActive: Boolean = true,
    val lastScannedStudent: StudentUiModel? = null,
    val attendanceCount: Int = 0,
    val errorMessage: String? = null
)
```

---

## 5. Integración de CameraX con Jetpack Compose

Ejemplo del componente contenedor del visor de cámara integrado con el analizador de código de barras:

```kotlin
@Composable
fun CameraPreviewView(
    modifier: Modifier = Modifier,
    isFlashEnabled: Boolean,
    onImageAnalyzerSet: (ImageAnalysis.Analyzer) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx ->
            PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
        },
        update = { previewView ->
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                // Se conecta el analizador expuesto por la capa de datos
                onImageAnalyzerSet { imageProxy ->
                    // Proceso de análisis
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    val camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                    camera.cameraControl.enableTorch(isFlashEnabled)
                } catch (exc: Exception) {
                    Log.e("CameraPreview", "Error binding camera lifecycle", exc)
                }
            }, ContextCompat.getMainExecutor(context))
        }
    )
}
```

---

## 6. Entregables del Rol por Sprint

1. **Pantallas Composables Reutilizables:** Código modularizado en componentes (`TopBar`, `Dialogs`, `Cards`, `Overlays`).
2. **ViewModels Inyectables:** ViewModels annotated con `@HiltViewModel` totalmente probables con pruebas de unidad de `UiState`.
3. **Módulo de Navegación Nativa:** Grafo de navegación tipo-seguro configurado mediante Kotlin Serialization / Jetpack Navigation Compose.
4. **Gestión de Permisos:** Componentes reactivos para solicitar y gestionar permisos en tiempo de ejecución (ej. usando `RememberPermissionState`).

---

## 7. Protocolo de Actuación del Skill

Cuando se invoque la habilidad del **Desarrollador Android Lead (Dev Mobile)**, la IA deberá:

1. **Escribir Código Nativo Kotlin Limpio:** Utilizar las mejores prácticas de Jetpack Compose, Material 3 y corrutinas.
2. **Implementar Patrones Reactivos:** Asegurar que toda mutación de UI provenga de un `UiState` expuesto por un `ViewModel`.
3. **Asegurar Manejo de Permisos y Ciclo de Vida:** Incluir siempre la gestión del ciclo de vida de Android (`LifecycleOwner`, `CameraX`) para evitar fugas de memoria o bloqueos de cámara.
4. **Integrar Feedback Háptico/Sonoro:** Conectar las respuestas táctiles y auditivas en el nivel de presentación tras recibir eventos del dominio.
