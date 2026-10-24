package com.dev.alt0191.shealthtweak.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.dev.alt0191.shealthtweak.SettingsPrefs


private val ExpressiveLight = lightColorScheme(
    primary = Color(0xFF00696D),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF6FF6FA),
    onPrimaryContainer = Color(0xFF00393B),   // tone-30 per Expressive onXContainer spec
    inversePrimary = Color(0xFF4CD9DE),
    secondary = Color(0xFF4A6267),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCCE8ED),
    onSecondaryContainer = Color(0xFF11343A),
    tertiary = Color(0xFF7A4E2E),             // warm accent
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFDCC2),
    onTertiaryContainer = Color(0xFF4A2709),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFF2FBFB),
    onBackground = Color(0xFF102122),
    surface = Color(0xFFF2FBFB),
    onSurface = Color(0xFF102122),
    surfaceVariant = Color(0xFFDAE4E5),
    onSurfaceVariant = Color(0xFF3F494A),
    surfaceTint = Color(0xFF00696D),
    inverseSurface = Color(0xFF2A3334),
    inverseOnSurface = Color(0xFFE3F1F1),
    outline = Color(0xFF6F797A),
    outlineVariant = Color(0xFFBEC8CA),
    surfaceContainer = Color(0xFFE6F2F2),
    surfaceContainerHigh = Color(0xFFDCE8E8),
    surfaceContainerHighest = Color(0xFFD1E2E2),
    surfaceContainerLow = Color(0xFFECF8F8),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceDim = Color(0xFFD1E2E2),
    surfaceBright = Color(0xFFF2FBFB),
)
private val ExpressiveDark = darkColorScheme(
    primary = Color(0xFF4CD9DE),
    onPrimary = Color(0xFF003739),
    primaryContainer = Color(0xFF004F52),
    onPrimaryContainer = Color(0xFF6FF6FA),
    inversePrimary = Color(0xFF00696D),
    secondary = Color(0xFFB0CCD2),
    onSecondary = Color(0xFF1B3438),
    secondaryContainer = Color(0xFF324B50),
    onSecondaryContainer = Color(0xFFCCE8ED),
    tertiary = Color(0xFFE7B48D),
    onTertiary = Color(0xFF46290E),
    tertiaryContainer = Color(0xFF5F3F1F),
    onTertiaryContainer = Color(0xFFFFDCC2),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF091819),
    onBackground = Color(0xFFD2E2E2),
    surface = Color(0xFF091819),
    onSurface = Color(0xFFD2E2E2),
    surfaceVariant = Color(0xFF3F494A),
    onSurfaceVariant = Color(0xFFBEC8CA),
    surfaceTint = Color(0xFF4CD9DE),
    inverseSurface = Color(0xFFD2E2E2),
    inverseOnSurface = Color(0xFF232B2C),
    outline = Color(0xFF889294),
    outlineVariant = Color(0xFF3F494A),
    surfaceContainer = Color(0xFF132223),
    surfaceContainerHigh = Color(0xFF1E2A2B),
    surfaceContainerHighest = Color(0xFF293536),
    surfaceContainerLow = Color(0xFF0E1D1E),
    surfaceContainerLowest = Color(0xFF041011),
    surfaceDim = Color(0xFF091819),
    surfaceBright = Color(0xFF2F3B3C),
)
private val ExpressiveShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(22.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp),
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppTheme(
    dark: Boolean = when (SettingsPrefs.darkMode) {
        SettingsPrefs.DARK_LIGHT -> false
        SettingsPrefs.DARK_DARK -> true
        else -> isSystemInDarkTheme()
    },
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        SettingsPrefs.dynamicColor &&
            android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S ->
            if (dark) dynamicDarkColorScheme(LocalContext.current)
            else dynamicLightColorScheme(LocalContext.current)

        dark -> ExpressiveDark
        else -> ExpressiveLight
    }

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        motionScheme = MotionScheme.expressive(),
        shapes = ExpressiveShapes,
        content = content,
    )
}
