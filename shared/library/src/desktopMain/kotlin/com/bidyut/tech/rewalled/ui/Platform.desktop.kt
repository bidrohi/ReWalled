package com.bidyut.tech.rewalled.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp

actual class PlatformContext

@Composable
actual fun getCurrentContext(): PlatformContext = PlatformContext()

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getSystemWidth(): Dp {
    val windowInfo = LocalWindowInfo.current
    return with(LocalDensity.current) {
        return windowInfo.containerSize.width.toDp()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun getSystemRatio(): Float {
    val windowInfo = LocalWindowInfo.current
    return windowInfo.containerSize.width.toFloat() / windowInfo.containerSize.height.toFloat()
}
