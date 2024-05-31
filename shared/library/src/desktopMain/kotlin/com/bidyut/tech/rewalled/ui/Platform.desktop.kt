package com.bidyut.tech.rewalled.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.rememberWindowState
import kotlin.math.roundToInt

actual class PlatformContext

@Composable
actual fun getCurrentContext(): PlatformContext = PlatformContext()

@Composable
actual fun getSystemWidthPx(): Int {
    val windowState = rememberWindowState()
    return windowState.size.width.value.roundToInt()
}

@Composable
actual fun getSystemRatio(): Float {
    val windowState = rememberWindowState()
    return windowState.size.width.value / windowState.size.height.value
}
