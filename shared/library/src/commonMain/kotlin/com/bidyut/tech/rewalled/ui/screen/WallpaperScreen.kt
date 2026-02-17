package com.bidyut.tech.rewalled.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.OpenInNew
import androidx.compose.material.icons.twotone.FileDownload
import androidx.compose.material.icons.twotone.Fullscreen
import androidx.compose.material.icons.twotone.FullscreenExit
import androidx.compose.material.icons.twotone.IosShare
import androidx.compose.material3.FabPosition
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.center
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.bidyut.tech.rewalled.model.SubredditFeedId
import com.bidyut.tech.rewalled.model.WallpaperId
import com.bidyut.tech.rewalled.ui.CONTENT_ANIMATION_DURATION
import com.bidyut.tech.rewalled.ui.components.BottomBar
import com.bidyut.tech.rewalled.ui.getCurrentContext
import com.bidyut.tech.rewalled.ui.getSystemWidth
import com.bidyut.tech.rewalled.ui.theme.ReWalledTheme
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

@Composable
fun SharedTransitionScope.WallpaperScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    feedId: SubredditFeedId,
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
        val screenWidthPx = with(LocalDensity.current) {
            getSystemWidth().roundToPx()
        }
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
            val hazeState = rememberHazeState()
            Scaffold(
                modifier = modifier,
                floatingActionButtonPosition = FabPosition.EndOverlay,
                floatingActionButton = {
                    if (isChromeShown) {
                        FloatingActionButton(onClick = {
                            feed.wallpapers.getOrNull(pagerState.currentPage)?.let { w ->
                                viewModel.coordinator.triggerDownloadIntent(context, w)
                            }
                        }) {
                            Icon(
                                Icons.TwoTone.FileDownload,
                                contentDescription = "Download",
                            )
                        }
                    }
                },
                bottomBar = {
                    AnimatedContent(
                        targetState = isChromeShown,
                        contentAlignment = Alignment.BottomCenter,
                        transitionSpec = {
                            fadeIn() + slideIn { it.center } togetherWith
                                    fadeOut() + slideOut { it.center }
                        }
                    ) { showBottomBar ->
                        if (showBottomBar) {
                            BottomBar(
                                hazeState = hazeState,
                                onBackClick = navigator::popBackStack,
                            ) {
                                IconButton(
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                        .size(48.dp),
                                    onClick = {
                                        isFullscreen = !isFullscreen
                                    },
                                ) {
                                    if (isFullscreen) {
                                        Icon(
                                            Icons.TwoTone.FullscreenExit,
                                            contentDescription = "Fit",
                                        )
                                    } else {
                                        Icon(
                                            Icons.TwoTone.Fullscreen,
                                            contentDescription = "Fill",
                                        )
                                    }
                                }
                                IconButton(
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                        .size(48.dp),
                                    onClick = {
                                        feed.wallpapers.getOrNull(pagerState.currentPage)
                                            ?.let { w ->
                                                viewModel.coordinator.triggerShareIntent(
                                                    context,
                                                    w
                                                )
                                            }
                                    },
                                ) {
                                    Icon(
                                        Icons.TwoTone.IosShare,
                                        contentDescription = "Share",
                                    )
                                }
                                IconButton(
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                        .size(48.dp),
                                    onClick = {
                                        feed.wallpapers.getOrNull(pagerState.currentPage)?.let {
                                            uriHandler.openUri(it.postUrl)
                                        }
                                    },
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.TwoTone.OpenInNew,
                                        contentDescription = "Open source",
                                    )
                                }
                            }
                        }
                    }
                }
            ) { paddingValues ->
                HorizontalPager(
                    modifier = Modifier.hazeSource(hazeState),
                    state = pagerState,
                    key = { feed.wallpapers[it].id }
                ) { index ->
                    val wallpaper = feed.wallpapers[index]
                    AnimatedContent(
                        targetState = isFullscreen,
                        transitionSpec = {
                            scaleIn() + fadeIn() togetherWith scaleOut() + fadeOut()
                        }
                    ) { isCropped ->
                        AsyncImage(
                            model = wallpaper.getUriForSize(screenWidthPx),
                            modifier = Modifier.fillMaxSize()
                                .sharedElement(
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    boundsTransform = { _, _ ->
                                        tween(durationMillis = CONTENT_ANIMATION_DURATION)
                                    },
                                    sharedContentState = rememberSharedContentState(
                                        key = "wallpaper-${wallpaper.id}"
                                    ),
                                ).clickable {
                                    isChromeShown = !isChromeShown
                                }.apply {
                                    if (!isCropped) {
                                        padding(paddingValues)
                                    }
                                },
                            contentDescription = wallpaper.summary,
                            contentScale = if (isCropped) {
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
}
