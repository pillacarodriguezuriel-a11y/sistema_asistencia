package pe.unsch.ceis.asistencia.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = InstitutionalGreenLight,
    onPrimary = OnInstitutionalGreenLight,
    primaryContainer = Color(0xFFA5D6A7),
    onPrimaryContainer = Color(0xFF08210C),
    secondary = JustifiedLight,
    onSecondary = Color.White,
    tertiary = LateLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    error = AbsentLight,
    onError = Color.White,
)

private val DarkColorScheme = darkColorScheme(
    primary = InstitutionalGreenDark,
    onPrimary = OnInstitutionalGreenDark,
    primaryContainer = Color(0xFF164A1A),
    onPrimaryContainer = Color(0xFFC6EBC7),
    secondary = JustifiedDark,
    onSecondary = Color(0xFF001E2E),
    tertiary = LateDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    error = AbsentDark,
    onError = Color(0xFF2D0002),
)

private val LocalAttendanceStatusColors = staticCompositionLocalOf {
    LightAttendanceStatusColors
}

val MaterialTheme.attendanceStatusColors: AttendanceStatusColors
    @Composable
    @ReadOnlyComposable
    get() = LocalAttendanceStatusColors.current

@Composable
fun AsistenciaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val attendanceColors =
        if (darkTheme) DarkAttendanceStatusColors else LightAttendanceStatusColors

    CompositionLocalProvider(LocalAttendanceStatusColors provides attendanceColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AsistenciaTypography,
            content = content,
        )
    }
}

