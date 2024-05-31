package com.bidyut.tech.rewalled.ui

import androidx.compose.runtime.Composable

expect class PlatformContext

@Composable
expect fun getCurrentContext(): PlatformContext

@Composable
expect fun getSystemWidthPx(): Int

@Composable
expect fun getSystemRatio(): Float
