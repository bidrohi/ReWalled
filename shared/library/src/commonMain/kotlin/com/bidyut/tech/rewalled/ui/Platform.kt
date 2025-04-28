package com.bidyut.tech.rewalled.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

expect class PlatformContext

@Composable
expect fun getCurrentContext(): PlatformContext

@Composable
expect fun getCachePath(): String

@Composable
expect fun getSystemWidth(): Dp

@Composable
expect fun getSystemRatio(): Float
