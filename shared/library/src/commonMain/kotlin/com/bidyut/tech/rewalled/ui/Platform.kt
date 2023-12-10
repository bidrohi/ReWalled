package com.bidyut.tech.rewalled.ui

import androidx.compose.runtime.Composable
import com.bidyut.tech.rewalled.model.Wallpaper

@Composable
expect fun getCurrentContext(): Any

@Composable
expect fun getSystemWidthPx(): Int

@Composable
expect fun getSystemRatio(): Float

expect fun triggerShareIntent(
    context: Any,
    w: Wallpaper,
)

expect fun triggerDownloadIntent(
    context: Any,
    w: Wallpaper,
)
