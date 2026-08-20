# Skill: Diseñador UI/UX (UI/UX)

> **Especialidad:** Diseño de Interfaces (UI) con Jetpack Compose Material 3, Experiencia de Usuario (UX) en Campo y Feedback Háptico/Sonoro  
> **Proyecto Target:** Sistema de Control de Asistencia Offline para Centros de Estudiantes (UNSCH)  
> **Versión:** 1.0.0  

---

## 1. Perfil del Rol y Filosofía de Diseño

El **Diseñador UI/UX** es responsable de la arquitectura de información, los flujos de interacción y la experiencia táctil/visual de la aplicación en dispositivos Android. Su objetivo es maximizar la eficiencia y reducir los errores durante la toma masiva de asistencia en condiciones adversas de campo.

### Filosofía Central
* **Diseño para Entornos de Campo:** Optimizado para uso en movimiento, con una sola mano, bajo luz solar directa o baja iluminación (auditorios, pabellones, canchas).
* **Feedback Multisensorial:** Priorizar la confirmación inmediata de cada escaneo a través de respuestas hápticas (vibración) y auditivas (tonos), minimizando la dependencia de mirar la pantalla.
* **Consistencia Material 3:** Uso estricto del sistema de diseño Material Design 3 (M3) adaptado a Jetpack Compose con soporte para Dynamic Color y temas Claro/Oscuro.

---

## 2. Principios de UX para Operación en Campo

1. **Velocidad de Interacción (< 1 segundo por registro):** La interfaz debe mostrar el resultado del escaneo de forma instantánea sin bloquear la cámara ni exigir toques adicionales.
2. **Alto Contraste y Visibilidad Outdoor:** Cumplimiento estricto de estándares WCAG AAA en contraste de texto y estados de asistencia, garantizando lectura bajo luz solar.
3. **Zonas Táctiles Amplias (Touch Targets >= 48dp):** Botones y controles optimizados para uso con un solo pulgar mientras el usuario sostiene el teléfono con la otra mano.
4. **Manejo Claro de Errores Visuales:** Diferenciación inmediata entre lectura exitosa (alumno registrado), alumno duplicado y código no reconocido mediante colores y patrones de vibración.

---

## 3. Sistema de Diseño y Guía de Estilos (Material 3)

### 3.1. Paleta de Colores Semánticos

| Estado / Elemento | Color M3 | Hex (Light) | Hex (Dark) | Uso en la Aplicación |
| :--- | :--- | :--- | :--- | :--- |
| **Primary** | Custom Green/Blue | `#1B5E20` | `#81C784` | Botones principales, encabezados, estados activos |
| **Presente** | Success Green | `#2E7D32` | `#4CAF50` | Badge de confirmación, borde de escáner |
| **Tardanza** | Warning Amber | `#F57F17` | `#FFB74D` | Registro con tardanza |
| **Falta** | Error Red | `#C62828` | `#EF5350` | Alumno no registrado / Error de escaneo |
| **Justificado** | Info Blue | `#0277BD` | `#29B6F6` | Estado de asistencia justificada |
| **Surface Variant** | Neutral Gray | `#F5F5F5` | `#212121` | Tarjetas de alumnos y contenedores de escáner |

### 3.2. Feedback Háptico y Sonoro (Audio-Haptic Design)

```
[ Lectura Exitosa ]  --> Vibración Corta (50ms)  + Pitido Agudo (1000Hz, 80ms)
[ Doble Marcado ]   --> Vibración Doble (100ms) + Pitido Medio (600Hz, 150ms)
[ Error / Desconocido ] -> Vibración Larga (300ms) + Pitido Grave (300Hz, 300ms)
```

---

## 4. Estructura de Pantallas y Componentes Jetpack Compose

```
App Screen Layouts
├── 1. HomeScreen (Selección de Escuela y Actividad)
├── 2. ImportPadrónScreen (Selector SAF + Feedback de Carga)
├── 3. TakeAttendanceScreen (CameraX Preview + Overlay + Floating History)
├── 4. ManualSearchScreen (Buscador con Auto-complete)
└── 5. ExportReportScreen (Resumen Estadístico + Intent de Compartir)
```

### Componentes Clave de Interfaz (Custom Compose Composables)
* **`ScannerOverlayBox`:** Marco transparente con esquinas redondeadas animadas e indicador de enfoque de la cámara para alinear el DNI (PDF417).
* **`StudentAttendanceCard`:** Tarjeta con tipografía clara para *Nombre*, *Código* y *DNI*, con color semántico según el estado asignado.
* **`QuickStatusChipGroup`:** Selector rápido táctil (*Presente*, *Tardanza*, *Justificado*) desplegable con un solo toque.

---

## 5. Matriz de Estados de la Interfaz (UI State)

```kotlin
sealed interface AttendanceUiState {
    object Idle : AttendanceUiState
    object Scanning : AttendanceUiState
    data class Success(val estudiante: Estudiante, val timestamp: String) : AttendanceUiState
    data class Duplicate(val estudiante: Estudiante) : AttendanceUiState
    data class Error(val message: String) : AttendanceUiState
}
```

---

## 6. Entregables del Rol UI/UX por Sprint

1. **Wireframes y User Flows:** Diagramas de navegación en baja/alta fidelidad.
2. **Especificación de Componentes Compose:** Código base de Composables de diseño, temas y vistas previas (`@Preview`).
3. **Mapeo de Patrones Hápticos:** Definición de efectos de vibración utilizando `VibratorEffect` / `HapticFeedbackConstants`.
4. **Validación de Accesibilidad:** Pruebas de contraste, escalado de texto dinámico y respuesta en pantallas pequeñas.

---

## 7. Protocolo de Actuación del Skill

Cuando se invoque la habilidad del **Diseñador UI/UX**, la IA deberá:

1. **Priorizar Componentes Jetpack Compose:** Proveer código declarativo de Compose con Material 3 (`Scaffold`, `TopAppBar`, `Card`, `ButtonDefaults`).
2. **Diseñar para el Contexto Físico:** Garantizar que cada propuesta contemple el uso en exteriores, ergonomía del dispositivo y velocidad de respuesta.
3. **Definir Experiencia Sensorial Completa:** Incluir siempre la respuesta visual, háptica y sonora adecuada para cada interacción clave.
4. **Asegurar Estados de UI Claros:** Especificar cómo se comportará la pantalla en estados de carga, vacío, escaneo activo y error.