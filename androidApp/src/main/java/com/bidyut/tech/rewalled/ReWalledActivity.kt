package com.bidyut.tech.rewalled

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bidyut.tech.rewalled.ui.Route
import com.bidyut.tech.rewalled.ui.screen.SubRedditScreen
import com.bidyut.tech.rewalled.ui.screen.SubRedditViewModel
import com.bidyut.tech.rewalled.ui.screen.WallpaperScreen

class ReWalledActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val subRedditViewModel: SubRedditViewModel = viewModel()
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Route.Grid.uri,
                modifier = Modifier.fillMaxSize(),
            ) {
                composable(
                    Route.Grid.uri,
                ) {
                    with(WindowCompat.getInsetsController(window, window.decorView)) {
                        show(WindowInsetsCompat.Type.systemBars())
                    }

                    SubRedditScreen(
                        navController = navController,
                        modifier = Modifier.fillMaxSize(),
                        viewModel = subRedditViewModel,
                    )
                }
                composable(
                    Route.Wallpaper("{id}").uri,
                    listOf(
                        navArgument("id") {
                            type = NavType.StringType
                        }
                    ),
                ) {
                    val id = it.arguments?.getString("id").orEmpty()

                    with(WindowCompat.getInsetsController(window, window.decorView)) {
                        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
                        hide(WindowInsetsCompat.Type.systemBars())
                    }

                    WallpaperScreen(
                        wallpaperId = id,
                        modifier = Modifier.fillMaxSize(),
                        viewModel = subRedditViewModel,
                    )
                }
            }
        }
    }
}
