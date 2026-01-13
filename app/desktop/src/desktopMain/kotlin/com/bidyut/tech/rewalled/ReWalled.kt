package com.bidyut.tech.rewalled

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.bidyut.tech.rewalled.di.AppGraph
import com.bidyut.tech.rewalled.di.DesktopAppGraph
import com.bidyut.tech.rewalled.ui.App

fun main() = application {
    AppGraph.assign(
        DesktopAppGraph(
            true,
        )
    )
    val windowState = rememberWindowState(size = DpSize(480.dp, 800.dp))
    Window(
        onCloseRequest = ::exitApplication,
        title = "ReWalled",
        state = windowState,
    ) {
        App()
    }
}
