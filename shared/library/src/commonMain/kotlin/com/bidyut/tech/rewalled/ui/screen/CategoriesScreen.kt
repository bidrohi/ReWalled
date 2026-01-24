package com.bidyut.tech.rewalled.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bidyut.tech.rewalled.model.Filter
import com.bidyut.tech.rewalled.model.SubredditFeedId
import com.bidyut.tech.rewalled.model.Wallpaper
import com.bidyut.tech.rewalled.model.WallpaperId
import com.bidyut.tech.rewalled.model.makeSubredditFeedId
import com.bidyut.tech.rewalled.ui.CONTENT_ANIMATION_DURATION
import com.bidyut.tech.rewalled.ui.Route
import com.bidyut.tech.rewalled.ui.getSystemRatio
import com.bidyut.tech.rewalled.ui.getSystemWidth
import com.bidyut.tech.rewalled.ui.screen.CategoriesViewModel.UiState
import com.bidyut.tech.rewalled.ui.theme.ReWalled
import com.bidyut.tech.rewalled.ui.theme.ReWalledTheme
import kotlin.math.roundToInt

@Composable
fun SharedTransitionScope.CategoriesScreen(
    navigator: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: CategoriesViewModel,
    modifier: Modifier = Modifier,
) {
    ReWalledTheme {
        Scaffold(
            modifier = modifier,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navigator.navigate(Route.Settings)
                    },
                    modifier = Modifier.sharedElement(
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = CONTENT_ANIMATION_DURATION)
                        },
                        sharedContentState = rememberSharedContentState(
                            key = "image-logo"
                        ),
                    ),
                ) {
                    Icon(
                        imageVector = ReWalled,
                        contentDescription = "About",
                    )
                }
            },
        ) { paddingValues ->
            val layoutDirection = LocalLayoutDirection.current
            val contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding() + 96.dp,
                start = paddingValues.calculateStartPadding(layoutDirection),
                end = paddingValues.calculateEndPadding(layoutDirection),
            )
            CategoriesPane(
                animatedVisibilityScope = animatedVisibilityScope,
                viewModel = viewModel,
                contentPadding = contentPadding,
                onCategoryClick = { feedId ->
                    navigator.navigate(Route.Grid(feedId))
                },
                onWallpaperClick = { feedId, wallpaperId ->
                    navigator.navigate(Route.Wallpaper(feedId, wallpaperId))
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
fun SharedTransitionScope.CategoriesPane(
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: CategoriesViewModel,
    contentPadding: PaddingValues,
    onCategoryClick: (SubredditFeedId) -> Unit,
    onWallpaperClick: (SubredditFeedId, WallpaperId) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {
        items(
            items = viewModel.subReddits,
            key = { it },
        ) { subReddit ->
            val feedId = makeSubredditFeedId(subReddit, Filter.Rising)
            val state = viewModel.getUiState(subReddit)
            CategoryRow(
                animatedVisibilityScope = animatedVisibilityScope,
                subReddit = subReddit,
                state = state.value,
                onWallpaperClick = { wallpaper ->
                    onWallpaperClick(feedId, wallpaper.id)
                },
                modifier = Modifier.fillMaxWidth()
                    .clickable {
                        onCategoryClick(feedId)
                    },
            )
        }
    }
}

@Composable
private fun SharedTransitionScope.CategoryRow(
    animatedVisibilityScope: AnimatedVisibilityScope,
    subReddit: String,
    state: UiState,
    onWallpaperClick: (Wallpaper) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    val gradientBrush by derivedStateOf {
        Brush.linearGradient(
            colors = listOf(
                colorScheme.primary,
                colorScheme.secondary,
                colorScheme.tertiary,
            ),
        )
    }
    AnimatedContent(
        targetState = state,
        transitionSpec = {
            slideInVertically { it } togetherWith slideOutVertically { -it }
        }
    ) { targetState ->
        when (targetState) {
            UiState.Loading -> CategoryLabel(
                subReddit = subReddit,
                modifier = modifier.padding(
                    horizontal = 16.dp,
                    vertical = 48.dp,
                ),
                textAlign = TextAlign.Center,
                textStyle = MaterialTheme.typography.titleLarge.plus(
                    SpanStyle(
                        brush = gradientBrush,
                    )
                ),
            )

            UiState.Error -> {}

            is UiState.ShowContent -> Column(
                modifier = modifier,
            ) {
                if (targetState.feed?.wallpapers?.isNotEmpty() == true) {
                    Text(
                        text = subReddit,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 8.dp,
                            ).sharedElement(
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ ->
                                    tween(durationMillis = CONTENT_ANIMATION_DURATION)
                                },
                                sharedContentState = rememberSharedContentState(
                                    key = "subreddit-$subReddit-title"
                                ),
                            ),
                    )
                    PhotoRow(
                        animatedVisibilityScope = animatedVisibilityScope,
                        wallpapers = targetState.feed.wallpapers,
                        onWallpaperClick = onWallpaperClick,
                        modifier = Modifier.fillMaxWidth()
                            .padding(
                                vertical = 8.dp,
                            ),
                    )
                }
            }
        }
    }
}

@Composable
fun SharedTransitionScope.PhotoRow(
    animatedVisibilityScope: AnimatedVisibilityScope,
    wallpapers: List<Wallpaper>,
    onWallpaperClick: (Wallpaper) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val ratio = getSystemRatio()
    val screenWidth = getSystemWidth()
    val wallpaperSize = 128.dp
    val inBetweenPadding = (-24).dp
    var maxImages by remember {
        mutableStateOf(((screenWidth) / (wallpaperSize + inBetweenPadding)).roundToInt())
    }
    LazyRow(
//        modifier = modifier.onSizeChanged {
//            maxImages = (it.width / wallpaperSize.value).toInt()
//        },
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(inBetweenPadding),
    ) {
        items(
            items = wallpapers.take(maxImages),
            key = { it.id },
        ) {
            WallpaperCard(
                wallpaper = it,
                requestWidth = screenWidth,
                imageRatio = ratio,
                cornerRadius = 4.dp,
                modifier = Modifier.width(wallpaperSize)
                    .sharedElement(
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = CONTENT_ANIMATION_DURATION)
                        },
                        sharedContentState = rememberSharedContentState(
                            key = "wallpaper-${it.id}"
                        ),
                    ).clickable { onWallpaperClick(it) },
            )
        }
    }
}
