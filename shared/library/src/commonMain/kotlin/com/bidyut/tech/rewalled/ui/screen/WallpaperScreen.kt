package com.bidyut.tech.rewalled.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.bidyut.tech.rewalled.model.WallpaperId
import com.bidyut.tech.rewalled.ui.getCurrentContext
import com.bidyut.tech.rewalled.ui.getSystemWidthPx
import com.bidyut.tech.rewalled.ui.theme.ReWalledTheme
import com.bidyut.tech.rewalled.ui.triggerDownloadIntent
import com.bidyut.tech.rewalled.ui.triggerShareIntent
import compose.icons.FeatherIcons
import compose.icons.feathericons.Download
import compose.icons.feathericons.ExternalLink
import compose.icons.feathericons.Maximize
import compose.icons.feathericons.Minimize
import compose.icons.feathericons.Share
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun WallpaperScreen(
    wallpaperId: WallpaperId,
    modifier: Modifier = Modifier,
    viewModel: SubRedditViewModel,
) {
    ReWalledTheme(
        isInDarkTheme = true,
    ) {
        val wallpaper = viewModel.getWallpaper(wallpaperId).collectAsState(initial = null)
        val screenWidthPx = getSystemWidthPx()
        var isFullscreen by remember {
            mutableStateOf(true)
        }
        val uriHandler = LocalUriHandler.current
        val context = getCurrentContext()
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.alpha(0.9f),
                    actions = {
                        IconButton(
                            modifier = Modifier.size(48.dp),
                            onClick = {
                                isFullscreen = !isFullscreen
                            },
                        ) {
                            if (isFullscreen) {
                                Icon(
                                    FeatherIcons.Minimize,
                                    contentDescription = "Fit",
                                )
                            } else {
                                Icon(
                                    FeatherIcons.Maximize,
                                    contentDescription = "Fill",
                                )
                            }
                        }
                        IconButton(
                            modifier = Modifier.size(48.dp),
                            onClick = {
                                wallpaper.value?.let { w ->
                                    triggerShareIntent(context, w)
                                }
                            },
                        ) {
                            Icon(
                                FeatherIcons.Share,
                                contentDescription = "Share",
                            )
                        }
                        IconButton(
                            modifier = Modifier.size(48.dp),
                            onClick = {
                                wallpaper.value?.let {
                                    uriHandler.openUri(it.postUrl)
                                }
                            },
                        ) {
                            Icon(
                                FeatherIcons.ExternalLink,
                                contentDescription = "Open source",
                            )
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
                            wallpaper.value?.let { w ->
                                triggerDownloadIntent(context, w)
                            }
                        }) {
                            Icon(
                                FeatherIcons.Download,
                                contentDescription = "Download",
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            wallpaper.value?.let {
                KamelImage(
                    resource = asyncPainterResource(it.getUriForSize(screenWidthPx)),
                    modifier = modifier.apply {
                        if (!isFullscreen) {
                            padding(paddingValues)
                        }
                    },
                    contentDescription = it.description,
                    contentScale = if (isFullscreen) {
                        ContentScale.Crop
                    } else {
                        ContentScale.Fit
                    },
                )
            }
        }
    }
}
