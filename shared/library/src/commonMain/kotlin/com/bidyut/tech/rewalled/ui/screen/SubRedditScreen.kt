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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
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
        Scaffold { paddingValues ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                val direction = LocalLayoutDirection.current
                val contentPadding = PaddingValues(
                    top = paddingValues.calculateTopPadding() + 8.dp,
                    bottom = paddingValues.calculateBottomPadding() + 80.dp,
                    start = paddingValues.calculateStartPadding(direction) + 8.dp,
                    end = paddingValues.calculateEndPadding(direction) + 8.dp,
                )
                val state = viewModel.getUiState(subReddit, filter)
                    .collectAsState(SubRedditViewModel.UiState.Loading)
                    .value
                SubRedditContents(
                    modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    state = state,
                    onWallpaperClick = {
                        navigator.navigate(
                            Route.Wallpaper(
                                makeFeedId(subReddit, filter),
                                it.id
                            ).uri
                        )
                    },
                    contentPadding = contentPadding,
                    onLoadMore = { afterCursor ->
                        viewModel.loadMoreAfter(subReddit, filter, afterCursor)
                    }
                )

                var isSubRedditMenuExpanded by remember {
                    mutableStateOf(false)
                }
                var mutableSubreddit by remember { mutableStateOf(subReddit) }
                val keyboardController = LocalSoftwareKeyboardController.current
                val focusManager = LocalFocusManager.current
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .align(Alignment.BottomCenter),
                    value = mutableSubreddit,
                    onValueChange = { mutableSubreddit = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        subReddit = mutableSubreddit
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        unfocusedPrefixColor = MaterialTheme.colorScheme.secondary,
                        focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        focusedPrefixColor = MaterialTheme.colorScheme.primary,
                    ),
                    prefix = {
                        Text(
                            text = "/r/",
                            fontWeight = FontWeight.Light,
                            modifier = Modifier.clickable {
                                isSubRedditMenuExpanded = true
                            },
                        )
                    },
                    trailingIcon = {
                        var isFilterMenuExpanded by remember {
                            mutableStateOf(false)
                        }
                        TextButton(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            onClick = { isFilterMenuExpanded = true },
                        ) {
                            Text(filter.toString())
                        }
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
                                        filter = value
                                        isFilterMenuExpanded = false
                                    },
                                )
                            }
                        }
                    },
                )
                DropdownMenu(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    expanded = isSubRedditMenuExpanded,
                    onDismissRequest = { isSubRedditMenuExpanded = false },
                ) {
                    for (value in SubReddit.defaults) {
                        val isSelected = subReddit == value
                        DropdownMenuItem(
                            text = {
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
                                        if (isSelected) {
                                            withStyle(
                                                style = SpanStyle(
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            ) {
                                                append(value)
                                            }
                                        } else {
                                            append(value)
                                        }
                                    }
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                subReddit = value
                                mutableSubreddit = value
                                isSubRedditMenuExpanded = false
                            },
                        )
                    }
                }
            }
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
                icon = FeatherIcons.Loader,
                description = "Loading contents",
                message = "Loading contents",
            )
        }

        is SubRedditViewModel.UiState.Error -> {
            InformationView(
                modifier = modifier.padding(contentPadding),
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
                contentPadding = contentPadding,
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
