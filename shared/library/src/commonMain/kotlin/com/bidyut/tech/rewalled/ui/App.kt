package com.bidyut.tech.rewalled.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bidyut.tech.rewalled.di.AppGraph
import com.bidyut.tech.rewalled.model.Filter
import com.bidyut.tech.rewalled.model.SubredditFeedId
import com.bidyut.tech.rewalled.model.WallpaperId
import com.bidyut.tech.rewalled.model.dissolve
import com.bidyut.tech.rewalled.model.makeSubredditFeedId
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
            startDestination = Route.Categories.uri,
            modifier = Modifier.fillMaxSize(),
        ) {
            composable(
                route = Route.Categories.uri,
            ) {
                CategoriesScreen(
                    navigator = navigator,
                    viewModel = categoriesViewModel,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            composable(
                route = Route.Settings.uri,
            ) {
                SettingsScreen(
                    navigator = navigator,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            composable(
                route = Route.SettingsCategories.uri,
            ) {
                CategoriesSettingsScreen(
                    navigator = navigator,
                    viewModel = categoriesViewModel,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            composable(
                route = Route.Grid("{feedId}").uri,
                arguments = listOf(
                    navArgument("feedId") {
                        type = NavType.StringType
                        defaultValue = makeSubredditFeedId("EarthPorn", Filter.Rising)
                    },
                ),
            ) {
                it.arguments?.getString("feedId")?.let { feedId ->
                    val (subreddit, filter) = feedId.dissolve()
                    subRedditViewModel.subReddit.value = subreddit
                    subRedditViewModel.filter.value = filter
                }
                SubRedditScreen(
                    navigator = navigator,
                    categoriesViewModel = categoriesViewModel,
                subRedditViewModel = subRedditViewModel,
                    modifier = Modifier.fillMaxSize(),
                )
            }
            composable(
                route = Route.Wallpaper("{feedId}", "{id}").uri,
                arguments = listOf(
                    navArgument("feedId") {
                        type = NavType.StringType
                        defaultValue = makeSubredditFeedId("EarthPorn", Filter.Rising)
                    },
                    navArgument("id") {
                        type = NavType.StringType
                    },
                ),
            ) {
                val feedId: SubredditFeedId = it.arguments?.getString("feedId").orEmpty()
                val id: WallpaperId = it.arguments?.getString("id").orEmpty()
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
