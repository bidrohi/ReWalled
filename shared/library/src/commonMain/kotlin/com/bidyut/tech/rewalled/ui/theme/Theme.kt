package com.bidyut.tech.rewalled.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

internal val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

internal val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ReWalledTheme(
    isInDarkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    isDynamicColorEnabled: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        isDynamicColorEnabled && isDynamicColorSupported() -> {
            handleDynamicColorScheme(isInDarkTheme)
        }

        isInDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    configureWindow(isInDarkTheme)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

internal expect fun isDynamicColorSupported(): Boolean

@Composable
internal expect fun handleDynamicColorScheme(
    isInDarkTheme: Boolean,
): ColorScheme

@Composable
internal expect fun configureWindow(
    isInDarkTheme: Boolean,
)
