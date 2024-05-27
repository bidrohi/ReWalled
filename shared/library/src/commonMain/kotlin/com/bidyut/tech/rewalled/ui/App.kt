package com.bidyut.tech.rewalled.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bidyut.tech.rewalled.ui.screen.SubRedditScreen
import com.bidyut.tech.rewalled.ui.screen.SubRedditViewModel
import com.bidyut.tech.rewalled.ui.screen.WallpaperScreen

@Composable
fun App() {
    val subRedditViewModel = viewModel { SubRedditViewModel() }
    val navigator: NavHostController = rememberNavController()
    NavHost(
        navController = navigator,
        startDestination = Route.Grid.uri,
        modifier = Modifier.fillMaxSize(),
    ) {
        composable(
            route = Route.Grid.uri,
        ) {
            SubRedditScreen(
                navigator = navigator,
                viewModel = subRedditViewModel,
                modifier = Modifier.fillMaxSize(),
            )
        }
        composable(
            route = Route.Wallpaper("{feedId}", "{id}").uri,
        ) {
            val feedId = it.arguments?.getString("feedId").orEmpty()
            val id = it.arguments?.getString("id").orEmpty()
            WallpaperScreen(
                feedId = feedId,
                wallpaperId = id,
                navigator = navigator,
                viewModel = subRedditViewModel,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
