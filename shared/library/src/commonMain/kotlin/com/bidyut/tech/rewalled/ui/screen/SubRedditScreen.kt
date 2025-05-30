package com.bidyut.tech.rewalled.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.twotone.ErrorOutline
import androidx.compose.material.icons.twotone.HourglassEmpty
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.window.core.layout.WindowWidthSizeClass
import com.bidyut.tech.rewalled.model.Filter
import com.bidyut.tech.rewalled.model.SubredditFeedId
import com.bidyut.tech.rewalled.model.Wallpaper
import com.bidyut.tech.rewalled.ui.Route
import com.bidyut.tech.rewalled.ui.theme.ReWalledTheme

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SubRedditScreen(
    navigator: NavController,
    categoriesViewModel: CategoriesViewModel,
    subRedditViewModel: SubRedditViewModel,
    modifier: Modifier = Modifier,
) {
    ReWalledTheme {
        val windowInfo = currentWindowAdaptiveInfo()
        if (windowInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
            var selectedFeedId by rememberSaveable {
                mutableStateOf<SubredditFeedId?>(subRedditViewModel.feedId)
            }
            ListDetailPaneScaffold(
                listPane = {
                    CategoriesPane(
                        viewModel = categoriesViewModel,
                        contentPadding = PaddingValues(bottom = 96.dp),
                        onCategoryClick = { feedId ->
                            selectedFeedId = feedId
                        },
                        onWallpaperClick = { feedId, wallpaperId ->
                            navigator.navigate(Route.Wallpaper(feedId, wallpaperId))
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                },
                detailPane = {
                    selectedFeedId?.let { feedId ->
                        SubRedditPane(
                            modifier = modifier,
                            subReddit = subRedditViewModel.subReddit.value,
                            filter = subRedditViewModel.filter.value,
                            uiState = subRedditViewModel.getUiState()
                                .collectAsState(SubRedditViewModel.UiState.Loading)
                                .value,
                            onFilterChange = {
                                subRedditViewModel.filter.value = it
                            },
                            onLoadMore = { afterCursor ->
                                subRedditViewModel.loadMoreAfter(afterCursor)
                            },
                            onWallpaperClick = {
                                navigator.navigate(
                                    Route.Wallpaper(subRedditViewModel.feedId, it.id)
                                )
                            },
                            onBackClick = {
                                navigator.popBackStack()
                            },
                        )
                    } ?: Box(
                        contentAlignment = Alignment.Center,
                        modifier = modifier.fillMaxSize(),
                    ) {
                        Text("No feed selected")
                    }
                },
                directive = PaneScaffoldDirective.Default,
                value = ThreePaneScaffoldValue(
                    primary = if (selectedFeedId != null) {
                        PaneAdaptedValue.Expanded
                    } else {
                        PaneAdaptedValue.Hidden
                    },
                    secondary = PaneAdaptedValue.Expanded,
                    tertiary = PaneAdaptedValue.Hidden,
                ),
                modifier = modifier,
            )
        } else {
            SubRedditPane(
                modifier = modifier,
                subReddit = subRedditViewModel.subReddit.value,
                filter = subRedditViewModel.filter.value,
                uiState = subRedditViewModel.getUiState()
                    .collectAsState(SubRedditViewModel.UiState.Loading)
                    .value,
                onFilterChange = {
                    subRedditViewModel.filter.value = it
                },
                onLoadMore = { afterCursor ->
                    subRedditViewModel.loadMoreAfter(afterCursor)
                },
                onWallpaperClick = {
                    navigator.navigate(
                        Route.Wallpaper(subRedditViewModel.feedId, it.id)
                    )
                },
                onBackClick = {
                    navigator.popBackStack()
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubRedditPane(
    subReddit: String,
    filter: Filter,
    uiState: SubRedditViewModel.UiState,
    onFilterChange: (Filter) -> Unit,
    onLoadMore: (String) -> Unit,
    onWallpaperClick: (Wallpaper) -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.alpha(0.8f),
                actions = {
                    if (onBackClick != null) {
                        IconButton(
                            modifier = Modifier.size(48.dp),
                            onClick = onBackClick,
                        ) {
                            Icon(
                                Icons.AutoMirrored.TwoTone.ArrowBack,
                                contentDescription = "Back",
                            )
                        }
                    }
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Light,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                append("/r/")
                            }
                            append(subReddit)
                        }
                    )
                    var isFilterMenuExpanded by remember {
                        mutableStateOf(false)
                    }
                    Text(
                        text = "[ $filter ]",
                        modifier = Modifier.clip(RoundedCornerShape(8.dp))
                            .clickable { isFilterMenuExpanded = true }
                            .padding(8.dp),
                    )
                    DropdownMenu(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        expanded = isFilterMenuExpanded,
                        onDismissRequest = { isFilterMenuExpanded = false },
                    ) {
                        for (value in Filter.entries) {
                            val isSelected = filter == value
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = if (isSelected) {
                                            value.toString().uppercase()
                                        } else {
                                            value.toString()
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.End,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontWeight = if (isSelected) FontWeight.ExtraBold else null,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified,
                                    )
                                },
                                onClick = {
                                    onFilterChange(value)
                                    isFilterMenuExpanded = false
                                },
                            )
                        }
                    }
                },
            )
        }
    ) { paddingValues ->
        val direction = LocalLayoutDirection.current
        val contentPadding = PaddingValues(
            top = paddingValues.calculateTopPadding() + 8.dp,
            bottom = paddingValues.calculateBottomPadding() + 8.dp,
            start = paddingValues.calculateStartPadding(direction) + 8.dp,
            end = paddingValues.calculateEndPadding(direction) + 8.dp,
        )
        SubRedditContents(
            modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            state = uiState,
            contentPadding = contentPadding,
            onWallpaperClick = onWallpaperClick,
            onLoadMore = onLoadMore,
        )
    }
}

@Composable
fun SubRedditContents(
    state: SubRedditViewModel.UiState,
    contentPadding: PaddingValues,
    onWallpaperClick: (Wallpaper) -> Unit,
    onLoadMore: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is SubRedditViewModel.UiState.Loading -> {
            InformationView(
                modifier = modifier.padding(contentPadding),
                icon = Icons.TwoTone.HourglassEmpty,
                description = "Loading contents",
                message = "Loading contents",
            )
        }

        is SubRedditViewModel.UiState.Error -> {
            InformationView(
                modifier = modifier.padding(contentPadding),
                icon = Icons.TwoTone.ErrorOutline,
                description = "Loading error",
                message = "Failed to load contents from the cloud",
            )
        }

        is SubRedditViewModel.UiState.ShowContent -> {
            PhotoGrid(
                modifier = modifier,
                wallpapers = state.feed?.wallpapers.orEmpty(),
                onWallpaperClick = onWallpaperClick,
                contentPadding = contentPadding,
                afterCursor = state.feed?.afterCursor,
                onLoadMore = {
                    onLoadMore(it)
                },
            )
        }
    }
}

@Composable
fun InformationView(
    icon: ImageVector,
    description: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            icon,
            contentDescription = description,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .padding(8.dp)
                .size(48.dp),
        )
        Text(text = message)
    }
}
