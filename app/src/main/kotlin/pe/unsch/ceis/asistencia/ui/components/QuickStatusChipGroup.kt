package pe.unsch.ceis.asistencia.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pe.unsch.ceis.asistencia.domain.model.EstadoAsistencia
import pe.unsch.ceis.asistencia.ui.theme.AsistenciaTheme

private val QuickStatuses = listOf(
    EstadoAsistencia.PRESENTE,
    EstadoAsistencia.TARDANZA,
    EstadoAsistencia.JUSTIFICADO,
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuickStatusChipGroup(
    selectedStatus: EstadoAsistencia,
    onStatusSelected: (EstadoAsistencia) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = "Estado de asistencia",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
        )
        FlowRow(
            modifier = Modifier.selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            QuickStatuses.forEach { status ->
                val visuals = status.visuals()
                val selected = selectedStatus == status
                val containerColor = if (selected) {
                    visuals.accent.copy(alpha = 0.16f)
                } else {
                    MaterialTheme.colorScheme.surface
                }

                Surface(
                    modifier = Modifier
                        .widthIn(min = 104.dp)
                        .heightIn(min = 48.dp)
                        .semantics {
                            stateDescription = if (selected) "Seleccionado" else "No seleccionado"
                        }
                        .selectable(
                            selected = selected,
                            enabled = enabled,
                            role = Role.RadioButton,
                            onClick = { onStatusSelected(status) },
                        ),
                    shape = RoundedCornerShape(24.dp),
                    color = containerColor,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    border = BorderStroke(
                        width = if (selected) 2.dp else 1.dp,
                        color = visuals.accent,
                    ),
                ) {
                    Text(
                        text = visuals.label,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

@Preview(
    name = "Selector rápido · Claro",
    showBackground = true,
    backgroundColor = 0xFFF9FAF7,
)
@Preview(
    name = "Selector rápido · Oscuro",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF0F1410,
)
@Composable
private fun QuickStatusChipGroupPreview() {
    AsistenciaTheme {
        QuickStatusChipGroup(
            selectedStatus = EstadoAsistencia.TARDANZA,
            onStatusSelected = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

