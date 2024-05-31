package com.bidyut.tech.rewalled.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.navigation.NavController
import com.bidyut.tech.rewalled.model.FeedId
import com.bidyut.tech.rewalled.model.WallpaperId
import com.bidyut.tech.rewalled.ui.getCurrentContext
import com.bidyut.tech.rewalled.ui.getSystemWidthPx
import com.bidyut.tech.rewalled.ui.theme.ReWalledTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.Download
import compose.icons.feathericons.ExternalLink
import compose.icons.feathericons.Maximize
import compose.icons.feathericons.Minimize
import compose.icons.feathericons.Share
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WallpaperScreen(
    feedId: FeedId,
    wallpaperId: WallpaperId,
    navigator: NavController,
    viewModel: SubRedditViewModel,
    modifier: Modifier = Modifier,
) {
    ReWalledTheme(
        isInDarkTheme = true,
    ) {
        var isFullscreen by remember {
            mutableStateOf(true)
        }
        var isChromeShown by remember {
            mutableStateOf(true)
        }
        val screenWidthPx = getSystemWidthPx()
        val context = getCurrentContext()
        val uriHandler = LocalUriHandler.current
        viewModel.getWallpaperFeed(feedId).collectAsState(initial = null).value?.let { feed ->
            var initialIndex = feed.wallpapers.indexOfFirst {
                it.id == wallpaperId
            }
            if (initialIndex == -1) {
                initialIndex = 0
            }
            val pagerState = rememberPagerState(
                initialPage = initialIndex,
                pageCount = { feed.wallpapers.size }
            )
            Scaffold(
                bottomBar = {
                    if (isChromeShown) {
                        BottomAppBar(
                            modifier = Modifier.alpha(0.9f),
                            actions = {
                                IconButton(
                                    modifier = Modifier.size(48.dp),
                                    onClick = {
                                        navigator.popBackStack()
                                    },
                                ) {
                                    Icon(
                                        FeatherIcons.ArrowLeft,
                                        contentDescription = "Back",
                                    )
                                }
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
                                        feed.wallpapers.getOrNull(pagerState.currentPage)?.let { w ->
                                            viewModel.coordinator.triggerShareIntent(context, w)
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
                                        feed.wallpapers.getOrNull(pagerState.currentPage)?.let {
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
                                    feed.wallpapers.getOrNull(pagerState.currentPage)?.let { w ->
                                        viewModel.coordinator.triggerDownloadIntent(context, w)
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
                }
            ) { paddingValues ->
                HorizontalPager(
                    state = pagerState,
                    key = { feed.wallpapers[it].id }
                ) { index ->
                    val wallpaper = feed.wallpapers[index]
                    KamelImage(
                        resource = asyncPainterResource(wallpaper.getUriForSize(screenWidthPx)),
                        modifier = modifier.clickable {
                            isChromeShown = !isChromeShown
                        }.apply {
                            if (!isFullscreen) {
                                padding(paddingValues)
                            }
                        },
                        contentDescription = wallpaper.summary,
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
}
