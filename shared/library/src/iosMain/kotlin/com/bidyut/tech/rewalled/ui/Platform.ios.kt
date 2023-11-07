package com.bidyut.tech.rewalled.ui

import androidx.compose.runtime.Composable
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.UIScreen

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun getSystemWidthPx(): Int {
    val screenWidth = UIScreen.mainScreen.bounds.useContents {
        size.width
    }
    val scale = UIScreen.mainScreen.scale
    return (screenWidth * scale).toInt()
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun getSystemRatio(): Float {
    val screenWidth = UIScreen.mainScreen.bounds.useContents {
        size.width
    }
    val screenHeight = UIScreen.mainScreen().bounds.useContents {
        size.height
    }
    return screenWidth.toFloat() / screenHeight.toFloat()
}
