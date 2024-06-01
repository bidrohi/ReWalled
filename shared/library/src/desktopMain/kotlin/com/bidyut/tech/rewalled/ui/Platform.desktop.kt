package com.bidyut.tech.rewalled.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.rememberWindowState

actual class PlatformContext

@Composable
actual fun getCurrentContext(): PlatformContext = PlatformContext()

@Composable
actual fun getSystemWidth(): Dp {
    val windowState = rememberWindowState()
    return windowState.size.width
}

@Composable
actual fun getSystemRatio(): Float {
    val windowState = rememberWindowState()
    return windowState.size.width.value / windowState.size.height.value
}
