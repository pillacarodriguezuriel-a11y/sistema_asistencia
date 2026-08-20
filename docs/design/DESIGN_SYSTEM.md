# Guía de estilos — Sistema de asistencia UNSCH

## Principios

- Uso en exteriores: texto base de 16–18 sp y contraste alto.
- Operación con una mano: controles interactivos de al menos 48 dp.
- Respuesta inmediata: todo resultado combina texto/color con feedback háptico
  y sonoro; ningún estado depende solo del color.
- Identidad estable: Dynamic Color está disponible, pero desactivado por defecto.

## Paleta institucional

| Token | Claro | Oscuro | Uso |
|---|---|---|---|
| Primary | `#1B5E20` | `#81C784` | Acciones críticas y foco |
| Surface | `#FFFFFF` | `#121212` | Tarjetas y controles |
| Surface Variant | `#F5F5F5` | `#212121` | Contenedores secundarios |
| Background | `#F9FAF7` | `#0F1410` | Fondo de pantalla |

## Colores semánticos

| Estado | Claro | Oscuro |
|---|---|---|
| Presente | `#2E7D32` | `#4CAF50` |
| Tardanza | `#F57F17` | `#FFB74D` |
| Falta | `#C62828` | `#EF5350` |
| Justificado | `#0277BD` | `#29B6F6` |

Para mantener contraste de lectura, el color semántico se utiliza como borde,
indicador y tinte suave. El texto permanece en `onSurface`; así se conserva el
significado oficial sin colocar texto pequeño directamente sobre un color que
no alcanza AAA.

## Tipografía

| Estilo | Tamaño / línea | Uso |
|---|---|---|
| Headline Large | 32 / 40 sp | Título de pantalla |
| Headline Medium | 28 / 36 sp | Sección principal |
| Title Large | 22 / 28 sp | Tarjetas destacadas |
| Title Medium | 18 / 24 sp | Nombre del estudiante |
| Body Large | 18 / 28 sp | Texto de campo |
| Body Medium | 16 / 24 sp | Metadatos |
| Label Large | 16 / 24 sp | Botones y chips |
| Label Medium | 14 / 20 sp | Etiquetas auxiliares |

Los usuarios pueden aumentar el tamaño desde accesibilidad Android; los
componentes no fijan alturas de texto que impidan escalado.

## Componentes

- `ScannerOverlayBox`: relación aproximada 1.58:1, esquinas pulsantes y línea de
  exploración para PDF417.
- `StudentAttendanceCard`: muestra los cuatro campos oficiales del padrón y un
  estado que combina etiqueta, borde y color.
- `QuickStatusChipGroup`: selector tipo radio, con targets mínimos de 48 dp y
  soporte de lector de pantalla.
- `PrimaryActionButton`: altura mínima de 56 dp, contraste institucional y estado
  de procesamiento no interactivo.

Cada archivo contiene previews para tema claro y oscuro.

## Feedback multisensorial

| Resultado | Vibración | Tono | Duración |
|---|---|---|---:|
| Éxito | 50 ms | DTMF alto | 80 ms |
| Duplicado | 100 ms, pausa, 100 ms | DTMF medio | 150 ms |
| Error | 300 ms | DTMF grave | 300 ms |

`AudioHapticHelper` implementa `Closeable`; la capa que controle su ciclo de
vida debe invocar `close()` para liberar `ToneGenerator` y cancelar vibraciones.

