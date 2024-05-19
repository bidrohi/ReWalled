package com.bidyut.tech.rewalled.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

internal val DarkColorScheme = darkColorScheme(
    primary = AntiFlashWhite,
    onPrimary = DarkGreen,
    secondary = Orange,
    onSecondary = AntiFlashWhite,
    tertiary = FederalBlue,
    onTertiary = AntiFlashWhite,
    background = Night,
    onBackground = AntiFlashWhite,
    surface = Night,
    onSurface = AntiFlashWhite,
)

internal val LightColorScheme = lightColorScheme(
    primary = CalPolyGreen,
    onPrimary = AntiFlashWhite,
    secondary = Mustard,
    onSecondary = Night,
    tertiary = HonoluluBlue,
    onTertiary = AntiFlashWhite,
    background = AntiFlashWhite,
    onBackground = Night,
    surface = AntiFlashWhite,
    onSurface = Night,
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
