package com.bidyut.tech.rewalled.ui.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import com.bidyut.tech.rewalled.model.Filter
import com.bidyut.tech.rewalled.model.SubredditFeedId
import com.bidyut.tech.rewalled.model.Wallpaper
import com.bidyut.tech.rewalled.model.WallpaperId
import com.bidyut.tech.rewalled.model.makeSubredditFeedId
import com.bidyut.tech.rewalled.ui.CONTENT_ANIMATION_DURATION
import com.bidyut.tech.rewalled.ui.getSystemRatio
import com.bidyut.tech.rewalled.ui.getSystemWidth
import com.bidyut.tech.rewalled.ui.screen.CategoriesViewModel.UiState
import com.bidyut.tech.rewalled.ui.theme.ReWalled
import com.bidyut.tech.rewalled.ui.theme.ReWalledTheme
import kotlin.math.roundToInt

@Composable
fun SharedTransitionScope.CategoriesScreen(
    viewModel: CategoriesViewModel,
    onCategoryClick: (SubredditFeedId) -> Unit,
    onWallpaperClick: (SubredditFeedId, WallpaperId) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ReWalledTheme {
        Scaffold(
            modifier = modifier,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onSettingsClick,
                    modifier = Modifier.sharedElement(
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
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
                viewModel = viewModel,
                contentPadding = contentPadding,
                onCategoryClick = onCategoryClick,
                onWallpaperClick = onWallpaperClick,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
fun SharedTransitionScope.CategoriesPane(
    viewModel: CategoriesViewModel,
    contentPadding: PaddingValues,
    onCategoryClick: (SubredditFeedId) -> Unit,
    onWallpaperClick: (SubredditFeedId, WallpaperId) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember {
        mutableStateOf<SubredditFeedId?>(null)
    }
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
                subReddit = subReddit,
                state = state.value,
                isSelected = feedId == selectedCategory,
                onWallpaperClick = { wallpaper ->
                    onWallpaperClick(feedId, wallpaper.id)
                },
                modifier = Modifier.fillMaxWidth()
                    .clickable {
                        selectedCategory = feedId
                        onCategoryClick(feedId)
                    },
            )
        }
    }
}

@Composable
private fun SharedTransitionScope.CategoryRow(
    subReddit: String,
    state: UiState,
    onWallpaperClick: (Wallpaper) -> Unit,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        modifier = Modifier.run {
            if (isSelected) {
                background(MaterialTheme.colorScheme.surfaceVariant)
            } else {
                this
            }
        },
        targetState = state,
        transitionSpec = {
            fadeIn() togetherWith slideOutVertically { -it }
        }
    ) { targetState ->
        when (targetState) {
            is UiState.Loading -> CategoryRow(
                subReddit = subReddit,
                modifier = modifier,
            ) {
                PhotoLoadingRow(
                    modifier = Modifier.fillMaxWidth()
                        .padding(
                            vertical = 8.dp,
                        ),
                )
            }

            is UiState.Error -> Unit

            is UiState.ShowContent -> if (targetState.feed?.wallpapers?.isNotEmpty() == true) {
                CategoryRow(
                    subReddit = subReddit,
                    modifier = modifier,
                ) {
                    PhotoRow(
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
fun SharedTransitionScope.CategoryRow(
    subReddit: String,
    modifier: Modifier = Modifier,
    content: @Composable SharedTransitionScope.() -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                ).sharedElement(
                    animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = CONTENT_ANIMATION_DURATION)
                    },
                    sharedContentState = rememberSharedContentState(
                        key = "subreddit-$subReddit-title"
                    ),
                ),
            text = subReddit,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        content()
    }
}

@Composable
fun SharedTransitionScope.PhotoRow(
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
    val visibleWallpapers by remember {
        derivedStateOf { wallpapers.take(maxImages) }
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
            items = visibleWallpapers,
            key = { it.id },
        ) {
            WallpaperCard(
                wallpaper = it,
                requestWidth = screenWidth,
                imageRatio = ratio,
                cornerRadius = 4.dp,
                modifier = Modifier.width(wallpaperSize)
                    .sharedElement(
                        animatedVisibilityScope = LocalNavAnimatedContentScope.current,
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

@Composable
fun PhotoLoadingRow(
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
        items(maxImages) { index ->
            Box(
                modifier = Modifier.width(wallpaperSize)
                    .aspectRatio(ratio)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        if (index % 2 == 0) {
                            MaterialTheme.colorScheme.surfaceContainerHighest
                        } else {
                            MaterialTheme.colorScheme.surfaceContainerHigh
                        }
                    ),
            )
        }
    }
}
