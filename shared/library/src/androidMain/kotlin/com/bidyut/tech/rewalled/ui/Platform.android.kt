package com.bidyut.tech.rewalled.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

actual class PlatformContext(
    val context: Context,
)

@Composable
actual fun getCurrentContext(): PlatformContext = PlatformContext(LocalContext.current)

@Composable
actual fun getSystemWidth(): Dp {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp.dp
}

@Composable
actual fun getSystemRatio(): Float {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp.toFloat() / configuration.screenHeightDp
}
