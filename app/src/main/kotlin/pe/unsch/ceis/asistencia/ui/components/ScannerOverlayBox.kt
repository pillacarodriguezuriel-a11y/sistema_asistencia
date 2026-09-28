package pe.unsch.ceis.asistencia.ui.components

import android.content.res.Configuration
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pe.unsch.ceis.asistencia.ui.theme.AsistenciaTheme

@Composable
fun ScannerOverlayBox(
    modifier: Modifier = Modifier,
    instruction: String = "Alinea el código PDF417 dentro del marco",
) {
    val transition = rememberInfiniteTransition(label = "scanner-overlay")
    val cornerPulse by transition.animateFloat(
        initialValue = 0.65f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "corner-pulse",
    )
    val scanProgress by transition.animateFloat(
        initialValue = 0.12f,
        targetValue = 0.88f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1_600),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "scan-progress",
    )
    val accent = MaterialTheme.colorScheme.primary

    Column(
        modifier = modifier.semantics {
            contentDescription = instruction
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.58f),
        ) {
            val cornerLength = size.minDimension * 0.20f
            val strokeWidth = 3.dp.toPx() * cornerPulse
            val left = strokeWidth
            val top = strokeWidth
            val right = size.width - strokeWidth
            val bottom = size.height - strokeWidth

            fun cornerLine(start: Offset, end: Offset) {
                drawLine(
                    color = accent.copy(alpha = cornerPulse),
                    start = start,
                    end = end,
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                )
            }

            cornerLine(Offset(left, top + cornerLength), Offset(left, top))
            cornerLine(Offset(left, top), Offset(left + cornerLength, top))
            cornerLine(Offset(right - cornerLength, top), Offset(right, top))
            cornerLine(Offset(right, top), Offset(right, top + cornerLength))
            cornerLine(Offset(left, bottom - cornerLength), Offset(left, bottom))
            cornerLine(Offset(left, bottom), Offset(left + cornerLength, bottom))
            cornerLine(Offset(right - cornerLength, bottom), Offset(right, bottom))
            cornerLine(Offset(right, bottom), Offset(right, bottom - cornerLength))

            val scanY = size.height * scanProgress
            drawLine(
                color = accent.copy(alpha = 0.72f),
                start = Offset(size.width * 0.12f, scanY),
                end = Offset(size.width * 0.88f, scanY),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round,
            )
        }

        Text(
            text = instruction,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(horizontal = 16.dp, vertical = 10.dp),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Preview(
    name = "Scanner · Claro",
    showBackground = true,
    backgroundColor = 0xFFF9FAF7,
)
@Preview(
    name = "Scanner · Oscuro",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF0F1410,
)
@Composable
private fun ScannerOverlayBoxPreview() {
    AsistenciaTheme {
        Box(
            modifier = Modifier
                .size(width = 360.dp, height = 300.dp)
                .padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            ScannerOverlayBox()
        }
    }
}

