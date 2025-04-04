package com.bidyut.tech.rewalled.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.bidyut.tech.rewalled.di.AppGraph
import com.bidyut.tech.rewalled.model.dissolve
import com.bidyut.tech.rewalled.ui.screen.CategoriesScreen
import com.bidyut.tech.rewalled.ui.screen.CategoriesSettingsScreen
import com.bidyut.tech.rewalled.ui.screen.CategoriesViewModel
import com.bidyut.tech.rewalled.ui.screen.SettingsScreen
import com.bidyut.tech.rewalled.ui.screen.SubRedditScreen
import com.bidyut.tech.rewalled.ui.screen.SubRedditViewModel
import com.bidyut.tech.rewalled.ui.screen.WallpaperScreen
import io.kamel.image.config.LocalKamelConfig

@Composable
fun App() {
    CompositionLocalProvider(LocalKamelConfig provides AppGraph.instance.kamelConfig) {
        val categoriesViewModel: CategoriesViewModel =
            viewModel(factory = CategoriesViewModel.factory())
        val subRedditViewModel: SubRedditViewModel =
            viewModel(factory = SubRedditViewModel.factory())
        val navigator: NavHostController = rememberNavController()
        NavHost(
            navController = navigator,
            startDestination = Route.Categories,
            modifier = Modifier.fillMaxSize(),
        ) {
            composable<Route.Categories> {
                CategoriesScreen(
                    navigator = navigator,
                    viewModel = categoriesViewModel,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            composable<Route.Settings> {
                SettingsScreen(
                    navigator = navigator,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            composable<Route.SettingsCategories> {
                CategoriesSettingsScreen(
                    navigator = navigator,
                    viewModel = categoriesViewModel,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            composable<Route.Grid> {
                val route = it.toRoute<Route.Grid>()
                val feedId = route.feedId
                val (subreddit, filter) = feedId.dissolve()
                subRedditViewModel.subReddit.value = subreddit
                subRedditViewModel.filter.value = filter
                SubRedditScreen(
                    navigator = navigator,
                    categoriesViewModel = categoriesViewModel,
                    subRedditViewModel = subRedditViewModel,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            composable<Route.Wallpaper> {
                val route = it.toRoute<Route.Wallpaper>()
                val feedId = route.feedId
                val id = route.id.orEmpty()
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
}
