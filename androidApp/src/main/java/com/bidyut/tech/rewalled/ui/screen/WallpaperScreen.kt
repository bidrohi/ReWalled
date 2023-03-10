package com.bidyut.tech.rewalled.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.bidyut.tech.rewalled.model.WallpaperId
import com.bidyut.tech.rewalled.ui.theme.ReWalledTheme

@Composable
fun WallpaperScreen(
    wallpaperId: WallpaperId,
    modifier: Modifier = Modifier,
    viewModel: SubRedditViewModel = viewModel(),
) {
    val wallpaper = viewModel.getWallpaper(wallpaperId).collectAsState(initial = null)
    val configuration = LocalConfiguration.current
    val screenWidthPx = with(LocalDensity.current) {
        configuration.screenWidthDp.dp.roundToPx()
    }
    ReWalledTheme(
        darkTheme = true,
    ) {
        AsyncImage(
            modifier = modifier,
            model = wallpaper.value?.getUriForSize(screenWidthPx),
            contentDescription = wallpaper.value?.description,
            contentScale = ContentScale.Crop,
        )
    }
}
