package pe.unsch.ceis.asistencia.ui.components.feedback

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import pe.unsch.ceis.asistencia.ui.state.PersistenceUiState
import pe.unsch.ceis.asistencia.ui.theme.AsistenciaTheme

const val PERSISTENCE_FEEDBACK_MILLIS = 1_800L

@Composable
fun PersistenceSnackbar(
    state: PersistenceUiState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentOnDismiss by rememberUpdatedState(onDismiss)

    LaunchedEffect(state) {
        if (state is PersistenceUiState.Success || state is PersistenceUiState.Error) {
            delay(PERSISTENCE_FEEDBACK_MILLIS)
            currentOnDismiss()
        }
    }

    AnimatedVisibility(
        visible = state !is PersistenceUiState.Idle,
        modifier = modifier,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        val visuals = state.visuals()
        Snackbar(
            modifier =
                Modifier.semantics {
                    liveRegion = LiveRegionMode.Polite
                },
            containerColor = visuals.containerColor,
            contentColor = visuals.contentColor,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (state is PersistenceUiState.Saving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = visuals.contentColor,
                        strokeWidth = 2.dp,
                    )
                }
                Text(
                    text = visuals.message,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

private data class PersistenceVisuals(
    val message: String,
    val containerColor: Color,
    val contentColor: Color,
)

@Composable
private fun PersistenceUiState.visuals(): PersistenceVisuals =
    when (this) {
        PersistenceUiState.Idle ->
            PersistenceVisuals(
                message = "",
                containerColor = Color.Transparent,
                contentColor = Color.Transparent,
            )

        PersistenceUiState.Saving ->
            PersistenceVisuals(
                message = "Guardando en el dispositivo…",
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            )

        is PersistenceUiState.Success ->
            PersistenceVisuals(
                message = message,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            )

        is PersistenceUiState.Error ->
            PersistenceVisuals(
                message = errorMsg,
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            )
    }

@Preview(
    name = "Persistencia · Claro",
    showBackground = true,
    backgroundColor = 0xFFF9FAF7,
)
@Preview(
    name = "Persistencia · Oscuro",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    backgroundColor = 0xFF0F1410,
)
@Composable
private fun PersistenceSnackbarPreview() {
    AsistenciaTheme {
        PersistenceSnackbar(
            state = PersistenceUiState.Success("Actividad guardada localmente"),
            onDismiss = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
