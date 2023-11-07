package com.bidyut.tech.rewalled.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
actual fun getSystemWidthPx(): Int {
    val configuration = LocalConfiguration.current
    return with(LocalDensity.current) {
        configuration.screenWidthDp.dp.roundToPx()
    }
}

@Composable
actual fun getSystemRatio(): Float {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp.toFloat() / configuration.screenHeightDp
}
