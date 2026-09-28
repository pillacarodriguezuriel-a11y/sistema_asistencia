package pe.unsch.ceis.asistencia.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import pe.unsch.ceis.asistencia.domain.model.EstadoAsistencia
import pe.unsch.ceis.asistencia.ui.theme.attendanceStatusColors

@Immutable
internal data class AttendanceStatusVisuals(
    val label: String,
    val accent: Color,
)

@Composable
internal fun EstadoAsistencia.visuals(): AttendanceStatusVisuals {
    val statusColors = MaterialTheme.attendanceStatusColors
    return when (this) {
        EstadoAsistencia.PRESENTE ->
            AttendanceStatusVisuals(
                label = "Presente",
                accent = statusColors.present,
            )

        EstadoAsistencia.TARDANZA ->
            AttendanceStatusVisuals(
                label = "Tardanza",
                accent = statusColors.late,
            )

        EstadoAsistencia.FALTA ->
            AttendanceStatusVisuals(
                label = "Falta",
                accent = statusColors.absent,
            )

        EstadoAsistencia.JUSTIFICADO ->
            AttendanceStatusVisuals(
                label = "Justificado",
                accent = statusColors.justified,
            )
    }
}
