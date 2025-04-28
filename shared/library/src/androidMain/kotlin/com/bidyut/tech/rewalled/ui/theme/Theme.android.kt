package com.bidyut.tech.rewalled.ui.theme

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat


internal actual fun isDynamicColorSupported(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}

@TargetApi(Build.VERSION_CODES.S)
@Composable
internal actual fun handleDynamicColorScheme(
    isInDarkTheme: Boolean,
): ColorScheme {
    val context = LocalContext.current
    return if (isInDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
}

@Composable
internal actual fun configureWindow(
    isInDarkTheme: Boolean,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
            insetsController.isAppearanceLightStatusBars = !isInDarkTheme
            insetsController.isAppearanceLightNavigationBars = !isInDarkTheme
        }
    }
}
