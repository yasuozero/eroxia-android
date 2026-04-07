package com.fecrin.eroxia.ui.theme

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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(

    primary = Primary500,
    onPrimary = White,
    primaryContainer = Primary900,
    onPrimaryContainer = Primary100,


    secondary = Zinc700,
    onSecondary = Zinc50,
    secondaryContainer = Zinc800,
    onSecondaryContainer = Zinc200,

    background = Zinc950,
    onBackground = Zinc50,

    surface = Zinc900,
    onSurface = Zinc50,
    surfaceVariant = Zinc800,
    onSurfaceVariant = Zinc300,

    error = Red500,
    onError = White,
    errorContainer = Red900,
    onErrorContainer = Red50,

    outline = Zinc700,
    outlineVariant = Zinc800,
    scrim = Zinc950
)

private val LightColorScheme = lightColorScheme(

    primary = Primary600,
    onPrimary = White,
    primaryContainer = Primary100,
    onPrimaryContainer = Primary900,

    secondary = Zinc200,
    onSecondary = Zinc900,
    secondaryContainer = Zinc100,
    onSecondaryContainer = Zinc900,

    background = Zinc50,
    onBackground = Zinc900,

    surface = White,
    onSurface = Zinc900,
    surfaceVariant = Zinc100,
    onSurfaceVariant = Zinc600,

    error = Red500,
    onError = White,
    errorContainer = Red50,
    onErrorContainer = Red900,

    outline = Zinc300,
    outlineVariant = Zinc200,
    scrim = Zinc950
)

@Composable
fun EroxiaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}