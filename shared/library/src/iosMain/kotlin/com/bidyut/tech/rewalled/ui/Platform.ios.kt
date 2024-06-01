package com.bidyut.tech.rewalled.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.interop.LocalUIViewController
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.UIScreen
import platform.UIKit.UIViewController

actual class PlatformContext(
    val viewController: UIViewController
)

@Composable
actual fun getCurrentContext(): PlatformContext = PlatformContext(LocalUIViewController.current)

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun getSystemWidth(): Dp {
    return UIScreen.mainScreen.bounds.useContents {
        size.width.dp
    }
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
