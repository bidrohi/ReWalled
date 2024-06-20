package com.bidyut.tech.rewalled.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.bidyut.tech.rewalled.model.Filter
import com.bidyut.tech.rewalled.model.Wallpaper
import com.bidyut.tech.rewalled.ui.Route
import com.bidyut.tech.rewalled.ui.theme.ReWalledTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubRedditScreen(
    navigator: NavController,
    viewModel: SubRedditViewModel,
    modifier: Modifier = Modifier,
) {
    ReWalledTheme {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        Scaffold(
            modifier = modifier,
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.alpha(0.8f),
                    actions = {
                        IconButton(
                            modifier = Modifier.size(48.dp),
                            onClick = {
                                navigator.popBackStack()
                            },
                        ) {
                            Icon(
                                Icons.AutoMirrored.TwoTone.ArrowBack,
                                contentDescription = "Back",
                            )
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
                                append(viewModel.subReddit.value)
                            }
                        )
                        var isFilterMenuExpanded by remember {
                            mutableStateOf(false)
                        }
                        Text(
                            text = "[ ${viewModel.filter.value} ]",
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
                                val isSelected = viewModel.filter.value == value
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
                                        viewModel.filter.value = value
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
            val state = viewModel.getUiState()
                .collectAsState(SubRedditViewModel.UiState.Loading)
                .value
            SubRedditContents(
                modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                state = state,
                onWallpaperClick = {
                    navigator.navigate(
                        Route.Wallpaper(viewModel.feedId, it.id).uri
                    )
                },
                contentPadding = contentPadding,
                onLoadMore = { afterCursor ->
                    viewModel.loadMoreAfter(afterCursor)
                }
            )
        }
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
