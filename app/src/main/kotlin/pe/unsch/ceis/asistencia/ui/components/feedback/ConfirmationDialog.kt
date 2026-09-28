package pe.unsch.ceis.asistencia.ui.components.feedback

import android.content.res.Configuration
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pe.unsch.ceis.asistencia.ui.theme.AsistenciaTheme

@Composable
fun ConfirmationDialog(
    visible: Boolean,
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    confirmLabel: String = "Eliminar",
    dismissLabel: String = "Cancelar",
) {
    if (!visible) return

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                modifier = Modifier.heightIn(min = 48.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                    ),
            ) {
                Text(confirmLabel)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.heightIn(min = 48.dp),
            ) {
                Text(dismissLabel)
            }
        },
    )
}

@Preview(
    name = "Confirmación · Claro",
    showBackground = true,
    backgroundColor = 0xFFF9FAF7,
)
@Preview(
    name = "Confirmación · Oscuro",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF0F1410,
)
@Composable
private fun ConfirmationDialogPreview() {
    AsistenciaTheme {
        ConfirmationDialog(
            visible = true,
            title = "¿Eliminar actividad?",
            message = "También se eliminarán todas sus asistencias. Esta acción no se puede deshacer.",
            onConfirm = {},
            onDismiss = {},
        )
    }
}
