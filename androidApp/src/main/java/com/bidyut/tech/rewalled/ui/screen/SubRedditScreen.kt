package com.bidyut.tech.rewalled.ui.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bidyut.tech.rewalled.R
import com.bidyut.tech.rewalled.model.Filter
import com.bidyut.tech.rewalled.model.Wallpaper
import com.bidyut.tech.rewalled.ui.Route
import com.bidyut.tech.rewalled.ui.theme.ReWalledTheme
import com.microsoft.fluent.mobile.icons.R as FluentR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubRedditScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SubRedditViewModel = viewModel(),
) {
    var subReddit by remember {
        mutableStateOf("Amoledbackgrounds")
    }
    var filter by remember {
        mutableStateOf(Filter.Rising)
    }
    var isFilterMenuExpanded by remember {
        mutableStateOf(false)
    }
    ReWalledTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("/r/$subReddit")
                    },
                    actions = {
                        Button(
                            onClick = { isFilterMenuExpanded = true },
                        ) {
                            Icon(
                                painter = painterResource(id = FluentR.drawable.ic_fluent_filter_24_regular),
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
                            for (value in Filter.values()) {
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
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                color = MaterialTheme.colorScheme.background
            ) {
                val state = viewModel.getUiState(subReddit, filter)
                    .collectAsState(SubRedditViewModel.UiState.Loading)
                    .value
                SubRedditContents(
                    modifier = modifier,
                    state = state,
                    onWallpaperClick = {
                        navController.navigate(Route.Wallpaper(it.id).uri)
                    },
                    onLoadMore = { afterCursor ->
                        viewModel.loadMoreAfter(subReddit, filter, afterCursor)
                    }
                )
            }
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
                    painter = painterResource(id = FluentR.drawable.ic_fluent_building_home_24_filled),
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
                    painter = painterResource(id = FluentR.drawable.ic_fluent_group_24_filled),
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
                iconId = FluentR.drawable.ic_fluent_cloud_sync_48_regular,
                descriptionId = R.string.loading_contents,
                messageId = R.string.loading_contents,
            )
        }

        is SubRedditViewModel.UiState.Error -> {
            InformationView(
                modifier = modifier,
                iconId = FluentR.drawable.ic_fluent_cloud_dismiss_48_regular,
                descriptionId = R.string.loading_error,
                messageId = R.string.loading_error_message,
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
    @DrawableRes iconId: Int,
    @StringRes descriptionId: Int,
    @StringRes messageId: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(iconId),
            contentDescription = stringResource(descriptionId),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .padding(8.dp)
                .size(48.dp),
        )
        Text(text = stringResource(messageId))
    }
}
