package pe.unsch.ceis.asistencia.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pe.unsch.ceis.asistencia.ui.components.OfflineReadyCard
import pe.unsch.ceis.asistencia.ui.theme.AsistenciaTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Text(
                text = "Sistema de asistencia",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "Centro de Estudiantes UNSCH",
                style = MaterialTheme.typography.bodyLarge,
            )
            OfflineReadyCard()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    AsistenciaTheme {
        HomeScreen()
    }
}

