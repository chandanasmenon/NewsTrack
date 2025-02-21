package com.chandana.newstrack.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF3A7BD5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF94DBFF),
    onPrimaryContainer = Color.Black,
    inversePrimary = Color(0xFF00D2FF),

    background = Color(0xFFF1F5FF),
    onBackground = Color(0xFF1A3A5F),

    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFE3EAF1),
    onSurfaceVariant = Color(0xFF495057),

    surfaceTint = Color(0xFF3A7BD5),
    inverseSurface = Color(0xFF1A3A5F),
    inverseOnSurface = Color.White,

    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF601410),

    outline = Color(0xFF8E9093),
    outlineVariant = Color(0xFFCAD4DE),
    scrim = Color.Black.copy(alpha = 0.32f)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF94DBFF),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF00578A),
    onPrimaryContainer = Color.White,
    inversePrimary = Color(0xFF3A7BD5),

    background = Color(0xFF121212),
    onBackground = Color(0xFFE1E1E1),

    surface = Color(0xFF1C1C1C),
    onSurface = Color(0xFFE1E1E1),
    surfaceVariant = Color(0xFF303030),
    onSurfaceVariant = Color(0xFFBFC1C2),

    surfaceTint = Color(0xFF94DBFF),
    inverseSurface = Color(0xFFE1E1E1),
    inverseOnSurface = Color(0xFF1C1C1C),

    error = Color(0xFFCF6679),
    onError = Color.Black,
    errorContainer = Color(0xFF8E0019),
    onErrorContainer = Color.White,

    outline = Color(0xFF666666),
    outlineVariant = Color(0xFF444444),
    scrim = Color.Black.copy(alpha = 0.8f)
)

@Composable
fun NewsAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}