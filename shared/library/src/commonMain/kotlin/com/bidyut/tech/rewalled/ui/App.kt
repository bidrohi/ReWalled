package com.bidyut.tech.rewalled.ui

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.bidyut.tech.rewalled.di.AppGraph
import com.bidyut.tech.rewalled.model.dissolve
import com.bidyut.tech.rewalled.ui.screen.CategoriesScreen
import com.bidyut.tech.rewalled.ui.screen.CategoriesSettingsScreen
import com.bidyut.tech.rewalled.ui.screen.CategoriesViewModel
import com.bidyut.tech.rewalled.ui.screen.GridPlaceholder
import com.bidyut.tech.rewalled.ui.screen.SettingsScreen
import com.bidyut.tech.rewalled.ui.screen.SubRedditScreen
import com.bidyut.tech.rewalled.ui.screen.SubRedditViewModel
import com.bidyut.tech.rewalled.ui.screen.WallpaperScreen
import okio.Path.Companion.toPath

const val CONTENT_ANIMATION_DURATION = 200

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun App() {
    val cachePath = getCachePath()
    setSingletonImageLoaderFactory { context ->
        val builder = ImageLoader.Builder(context)
            .crossfade(true)
            .diskCache {
                DiskCache.Builder()
                    .directory(cachePath.toPath(true))
                    .maxSizeBytes(100 * 1024 * 1024)
                    .build()
            }
        if (AppGraph.instance.enableDebug) {
            builder.logger(DebugLogger())
        }
        builder.build()
    }

    val categoriesViewModel: CategoriesViewModel =
        viewModel(factory = CategoriesViewModel.factory())
    val subRedditViewModel: SubRedditViewModel =
        viewModel(factory = SubRedditViewModel.factory())
    val backStack = remember { mutableStateListOf<Route>(Route.Categories) }
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val directive = remember(windowAdaptiveInfo) {
        calculatePaneScaffoldDirective(windowAdaptiveInfo)
            .copy(horizontalPartitionSpacerSize = 0.dp)
    }
    val listDetailStrategy = rememberListDetailSceneStrategy<Route>(
        directive = directive,
    )
    SharedTransitionLayout {
        NavDisplay(
            backStack = backStack,
            sceneStrategy = listDetailStrategy,
            onBack = backStack::removeLastOrNull,
            entryProvider = entryProvider {
                entry<Route.Categories>(
                    metadata = ListDetailSceneStrategy.listPane {
                        GridPlaceholder(
                            modifier = Modifier.fillMaxSize(),
                        )
                    },
                ) {
                    CategoriesScreen(
                        viewModel = categoriesViewModel,
                        onCategoryClick = { feedId ->
                            backStack.removeAll { it is Route.Grid }
                            backStack.add(Route.Grid(feedId))
                        },
                        onWallpaperClick = { feedId, wallpaperId ->
                            backStack.add(Route.Wallpaper(feedId, wallpaperId))
                        },
                        onSettingsClick = {
                            backStack.add(Route.Settings)
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                entry<Route.Settings> {
                    SettingsScreen(
                        onSettingsCategoryClick = {
                            backStack.add(Route.SettingsCategories)
                        },
                        onBackClick = backStack::removeLastOrNull,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                entry<Route.SettingsCategories> {
                    CategoriesSettingsScreen(
                        viewModel = categoriesViewModel,
                        onBackClick = backStack::removeLastOrNull,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                entry<Route.Grid>(
                    metadata = ListDetailSceneStrategy.detailPane(),
                ) { key ->
                    val feedId = key.feedId
                    val (subreddit, filter) = feedId.dissolve()
                    subRedditViewModel.subReddit.value = subreddit
                    subRedditViewModel.filter.value = filter
                    SubRedditScreen(
                        subRedditViewModel = subRedditViewModel,
                        onWallpaperClick = { feedId, wallpaperId ->
                            backStack.add(Route.Wallpaper(feedId, wallpaperId))
                        },
                        onBackClick = backStack::removeLastOrNull,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                entry<Route.Wallpaper> { key ->
                    val feedId = key.feedId
                    val id = key.id.orEmpty()
                    WallpaperScreen(
                        feedId = feedId,
                        wallpaperId = id,
                        viewModel = subRedditViewModel,
                        onBackClick = backStack::removeLastOrNull,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        )
    }
}
