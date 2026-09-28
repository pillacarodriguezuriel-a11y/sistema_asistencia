package pe.unsch.ceis.asistencia.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

internal val InstitutionalGreenLight = Color(0xFF1B5E20)
internal val InstitutionalGreenDark = Color(0xFF81C784)
internal val OnInstitutionalGreenLight = Color(0xFFFFFFFF)
internal val OnInstitutionalGreenDark = Color(0xFF06210B)

internal val PresentLight = Color(0xFF2E7D32)
internal val PresentDark = Color(0xFF4CAF50)
internal val LateLight = Color(0xFFF57F17)
internal val LateDark = Color(0xFFFFB74D)
internal val AbsentLight = Color(0xFFC62828)
internal val AbsentDark = Color(0xFFEF5350)
internal val JustifiedLight = Color(0xFF0277BD)
internal val JustifiedDark = Color(0xFF29B6F6)

internal val SurfaceLight = Color(0xFFFFFFFF)
internal val SurfaceDark = Color(0xFF121212)
internal val SurfaceVariantLight = Color(0xFFF5F5F5)
internal val SurfaceVariantDark = Color(0xFF212121)
internal val BackgroundLight = Color(0xFFF9FAF7)
internal val BackgroundDark = Color(0xFF0F1410)
internal val OnSurfaceLight = Color(0xFF111411)
internal val OnSurfaceDark = Color(0xFFF1F5F1)
internal val OutlineLight = Color(0xFF4D564E)
internal val OutlineDark = Color(0xFFB8C2B9)

@Immutable
data class AttendanceStatusColors(
    val present: Color,
    val late: Color,
    val absent: Color,
    val justified: Color,
)

internal val LightAttendanceStatusColors = AttendanceStatusColors(
    present = PresentLight,
    late = LateLight,
    absent = AbsentLight,
    justified = JustifiedLight,
)

internal val DarkAttendanceStatusColors = AttendanceStatusColors(
    present = PresentDark,
    late = LateDark,
    absent = AbsentDark,
    justified = JustifiedDark,
)
