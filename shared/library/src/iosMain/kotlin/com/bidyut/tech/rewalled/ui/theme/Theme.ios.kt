package com.bidyut.tech.rewalled.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

internal actual fun isDynamicColorSupported(): Boolean {
    return false
}

@Composable
internal actual fun handleDynamicColorScheme(
    isInDarkTheme: Boolean,
): ColorScheme {
    return if (isInDarkTheme) DarkColorScheme else LightColorScheme
}

@Composable
internal actual fun configureWindow(
    isInDarkTheme: Boolean,
) {
}
