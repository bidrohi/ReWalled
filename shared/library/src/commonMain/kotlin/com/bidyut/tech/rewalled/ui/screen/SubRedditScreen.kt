package com.bidyut.tech.rewalled.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bidyut.tech.rewalled.model.Filter
import com.bidyut.tech.rewalled.model.SubReddit
import com.bidyut.tech.rewalled.model.Wallpaper
import com.bidyut.tech.rewalled.model.makeFeedId
import com.bidyut.tech.rewalled.ui.Route
import com.bidyut.tech.rewalled.ui.theme.ReWalledTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.AlertOctagon
import compose.icons.feathericons.Filter
import compose.icons.feathericons.Grid
import compose.icons.feathericons.Home
import compose.icons.feathericons.Loader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubRedditScreen(
    navigator: NavController,
    viewModel: SubRedditViewModel,
    modifier: Modifier = Modifier,
) {
    var subReddit by remember {
        mutableStateOf("Amoledbackgrounds")
    }
    var filter by remember {
        mutableStateOf(Filter.Rising)
    }
    ReWalledTheme {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        Scaffold(
            topBar = {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = {
                        var isSubRedditMenuExpanded by remember {
                            mutableStateOf(false)
                        }
                        Text(
                            text = "/r/$subReddit",
                            modifier = Modifier.clickable {
                                isSubRedditMenuExpanded = true
                            }
                        )
                        DropdownMenu(
                            expanded = isSubRedditMenuExpanded,
                            onDismissRequest = { isSubRedditMenuExpanded = false },
                        ) {
                            for (value in SubReddit.defaults) {
                                DropdownMenuItem(
                                    text = { Text(value) },
                                    onClick = {
                                        subReddit = value
                                        isSubRedditMenuExpanded = false
                                    },
                                )
                            }
                        }
                    },
                    actions = {
                        var isFilterMenuExpanded by remember {
                            mutableStateOf(false)
                        }
                        TextButton(
                            onClick = { isFilterMenuExpanded = true },
                        ) {
                            Icon(
                                FeatherIcons.Filter,
                                contentDescription = "Filter",
                            )
                            Text(
                                modifier = Modifier.padding(horizontal = 4.dp),
                                text = filter.toString(),
                            )
                        }
                        DropdownMenu(
                            expanded = isFilterMenuExpanded,
                            onDismissRequest = { isFilterMenuExpanded = false },
                        ) {
                            for (value in Filter.entries) {
                                DropdownMenuItem(
                                    text = { Text(value.toString()) },
                                    onClick = {
                                        filter = value
                                        isFilterMenuExpanded = false
                                    },
                                )
                            }
                        }
                    },
                )
            },
//            bottomBar = { MainNavigationBar() }
        ) { paddingValues ->
            val state = viewModel.getUiState(subReddit, filter)
                .collectAsState(SubRedditViewModel.UiState.Loading)
                .value
            SubRedditContents(
                modifier = modifier
                    .padding(paddingValues)
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                state = state,
                onWallpaperClick = {
                    navigator.navigate(Route.Wallpaper(makeFeedId(subReddit, filter), it.id).uri)
                },
                onLoadMore = { afterCursor ->
                    viewModel.loadMoreAfter(subReddit, filter, afterCursor)
                }
            )
        }
    }
}

@Composable
fun MainNavigationBar() {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            icon = {
                Image(
                    FeatherIcons.Home,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
                )
            },
            label = {
                Text("Home")
            },
            onClick = { /*TODO*/ },
        )
        NavigationBarItem(
            selected = false,
            icon = {
                Image(
                    FeatherIcons.Grid,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer),
                )
            },
            label = {
                Text("Categories")
            },
            onClick = { /*TODO*/ },
        )
    }
}

@Composable
fun SubRedditContents(
    state: SubRedditViewModel.UiState,
    onWallpaperClick: (Wallpaper) -> Unit,
    onLoadMore: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        is SubRedditViewModel.UiState.Loading -> {
            InformationView(
                modifier = modifier,
                icon = FeatherIcons.Loader,
                description = "Loading contents",
                message = "Loading contents",
            )
        }

        is SubRedditViewModel.UiState.Error -> {
            InformationView(
                modifier = modifier,
                icon = FeatherIcons.AlertOctagon,
                description = "Loading error",
                message = "Failed to load contents from the cloud",
            )
        }

        is SubRedditViewModel.UiState.ShowContent -> {
            PhotoGrid(
                modifier = modifier,
                wallpapers = state.feed.wallpapers,
                onWallpaperClick = onWallpaperClick,
                hasMore = state.feed.afterCursor != null,
                onLoadMore = {
                    onLoadMore(state.feed.afterCursor!!)
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
