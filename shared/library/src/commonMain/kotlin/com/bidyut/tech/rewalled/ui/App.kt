package com.bidyut.tech.rewalled.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bidyut.tech.rewalled.ui.screen.SubRedditScreen
import com.bidyut.tech.rewalled.ui.screen.SubRedditViewModel
import com.bidyut.tech.rewalled.ui.screen.WallpaperScreen
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.viewmodel.viewModel

@Composable
fun App() {
    val subRedditViewModel = viewModel(SubRedditViewModel::class) {
        SubRedditViewModel()
    }
    val navigator = rememberNavigator()
    NavHost(
        navigator = navigator,
        initialRoute = Route.Grid.uri,
        modifier = Modifier.fillMaxSize(),
    ) {
        scene(
            route = Route.Grid.uri,
        ) {
            SubRedditScreen(
                navigator = navigator,
                modifier = Modifier.fillMaxSize(),
                viewModel = subRedditViewModel,
            )
        }
        scene(
            route = Route.Wallpaper("{id}").uri,
        ) {
            val id = it.path<String>("id").orEmpty()
            WallpaperScreen(
                wallpaperId = id,
                modifier = Modifier.fillMaxSize(),
                viewModel = subRedditViewModel,
            )
        }
    }
}
