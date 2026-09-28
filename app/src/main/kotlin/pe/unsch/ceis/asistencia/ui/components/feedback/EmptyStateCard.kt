package pe.unsch.ceis.asistencia.ui.components.feedback

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pe.unsch.ceis.asistencia.ui.theme.AsistenciaTheme

@Composable
fun EmptyStateCard(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    illustrationDescription: String = "Lista vacía",
) {
    val accent = MaterialTheme.colorScheme.primary
    val mutedAccent = MaterialTheme.colorScheme.outline

    Card(
        modifier = modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Canvas(
                modifier =
                    Modifier
                        .size(88.dp)
                        .semantics {
                            contentDescription = illustrationDescription
                        },
            ) {
                val lineWidth = 5.dp.toPx()
                drawRoundRect(
                    color = accent,
                    topLeft = Offset(size.width * 0.18f, size.height * 0.12f),
                    size = Size(size.width * 0.64f, size.height * 0.76f),
                    cornerRadius =
                        androidx.compose.ui.geometry
                            .CornerRadius(10.dp.toPx()),
                    style = Stroke(width = lineWidth),
                )
                repeat(3) { index ->
                    val y = size.height * (0.34f + index * 0.17f)
                    drawLine(
                        color = mutedAccent,
                        start = Offset(size.width * 0.34f, y),
                        end = Offset(size.width * 0.68f, y),
                        strokeWidth = lineWidth,
                        cap = StrokeCap.Round,
                    )
                }
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(
    name = "Estado vacío · Claro",
    showBackground = true,
    backgroundColor = 0xFFF9FAF7,
)
@Preview(
    name = "Estado vacío · Oscuro",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF0F1410,
)
@Composable
private fun EmptyStateCardPreview() {
    AsistenciaTheme {
        EmptyStateCard(
            title = "Aún no hay actividades",
            message = "Crea una actividad para comenzar a registrar asistencias.",
            modifier = Modifier.padding(16.dp),
        )
    }
}
