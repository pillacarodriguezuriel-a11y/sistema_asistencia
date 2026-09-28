package pe.unsch.ceis.asistencia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import pe.unsch.ceis.asistencia.ui.navigation.AppNavHost
import pe.unsch.ceis.asistencia.ui.theme.AsistenciaTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AsistenciaTheme {
                AppNavHost()
            }
        }
    }
}

