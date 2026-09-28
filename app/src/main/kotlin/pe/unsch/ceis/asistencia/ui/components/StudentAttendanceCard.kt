package pe.unsch.ceis.asistencia.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pe.unsch.ceis.asistencia.domain.model.EstadoAsistencia
import pe.unsch.ceis.asistencia.ui.theme.AsistenciaTheme

@Composable
fun StudentAttendanceCard(
    nombresApellidos: String,
    codigo: String,
    dni: String,
    correoInstitucional: String,
    estado: EstadoAsistencia,
    modifier: Modifier = Modifier,
) {
    val status = estado.visuals()

    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .semantics(mergeDescendants = true) {
                    stateDescription = status.label
                },
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    text = nombresApellidos,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                )
                Surface(
                    modifier = Modifier.widthIn(min = 92.dp),
                    shape = RoundedCornerShape(50),
                    color = status.accent.copy(alpha = 0.14f),
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    border = BorderStroke(2.dp, status.accent),
                ) {
                    Text(
                        text = status.label,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }

            StudentField(label = "Código", value = codigo)
            StudentField(label = "DNI", value = dni)
            StudentField(label = "Correo institucional", value = correoInstitucional)
        }
    }
}

@Composable
private fun StudentField(
    label: String,
    value: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium,
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview(
    name = "Tarjeta estudiante · Claro",
    showBackground = true,
    backgroundColor = 0xFFF9FAF7,
)
@Preview(
    name = "Tarjeta estudiante · Oscuro",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF0F1410,
)
@Composable
private fun StudentAttendanceCardPreview() {
    AsistenciaTheme {
        StudentAttendanceCard(
            nombresApellidos = "Quispe Flores, Ana Lucía",
            codigo = "000123",
            dni = "01234567",
            correoInstitucional = "ana.quispe@unsch.edu.pe",
            estado = EstadoAsistencia.PRESENTE,
            modifier = Modifier.padding(16.dp),
        )
    }
}
