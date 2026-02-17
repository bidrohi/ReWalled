package com.bidyut.tech.rewalled.ui.screen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.AddCircle
import androidx.compose.material.icons.twotone.RemoveCircle
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bidyut.tech.rewalled.ui.CONTENT_ANIMATION_DURATION
import com.bidyut.tech.rewalled.ui.components.BottomBar
import com.bidyut.tech.rewalled.ui.theme.ReWalledTheme
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun SharedTransitionScope.CategoriesSettingsScreen(
    navigator: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: CategoriesViewModel,
    modifier: Modifier = Modifier
) {
    ReWalledTheme {
        var showAddSheet by remember { mutableStateOf(false) }

        if (showAddSheet) {
            AddCategoryBottomSheet(
                onDismiss = {
                    showAddSheet = false
                },
                onAddSubReddit = {
                    viewModel.subReddits.add(it)
                    showAddSheet = false
                }
            )
        }

        val hazeState = rememberHazeState()
        Scaffold(
            modifier = modifier,
            floatingActionButtonPosition = FabPosition.EndOverlay,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        showAddSheet = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.AddCircle,
                        contentDescription = "Add category",
                    )
                }
            },
            bottomBar = {
                BottomBar(
                    hazeState = hazeState,
                    onBackClick = navigator::popBackStack,
                ) {
                    Text(
                        text = "Configure categories",
                        modifier = Modifier.padding(end = 32.dp)
                            .sharedElement(
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ ->
                                    tween(durationMillis = CONTENT_ANIMATION_DURATION)
                                },
                                sharedContentState = rememberSharedContentState(
                                    key = "categories-config-title"
                                ),
                            ),
                    )
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .hazeSource(hazeState),
                contentPadding = paddingValues,
            ) {
                items(
                    items = viewModel.subReddits,
                    key = { it },
                ) { subReddit ->
                    CategoryLabel(
                        subReddit = subReddit,
                        modifier = modifier.padding(vertical = 16.dp),
                        leadingContent = {
                            IconButton(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                onClick = {
                                    viewModel.subReddits.remove(subReddit)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.TwoTone.RemoveCircle,
                                    tint = MaterialTheme.colorScheme.error,
                                    contentDescription = "Remove category",
                                )
                            }
                        },
//                        trailingContent = {
//                            Icon(
//                                imageVector = Icons.TwoTone.Reorder,
//                                tint = MaterialTheme.colorScheme.outlineVariant,
//                                contentDescription = "Move category",
//                                modifier = Modifier.padding(horizontal = 16.dp),
//                            )
//                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddCategoryBottomSheet(
    onDismiss: () -> Unit = {},
    onAddSubReddit: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val modalBottomSheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Text(
            text = "Add new category",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Text(
            text = """
                Since images on Reddit is not classified as wallpapers or other types of images, the app shows any image provided in the post. Try out different subreddits to find what works for you.
                NOTE: Any NSFW content flagged by Reddit will be filtered.
            """.trimIndent(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp),
        )
        var subReddit by remember { mutableStateOf("") }
        OutlinedTextField(
            value = subReddit,
            onValueChange = {
                subReddit = it
            },
            prefix = {
                Text(
                    text = "/r/",
                    fontWeight = FontWeight.Light,
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.TwoTone.Add,
                    contentDescription = "Add",
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions {
                onAddSubReddit(subReddit)
            },
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
                .focusRequester(focusRequester),
        )
        FilledTonalButton(
            enabled = subReddit.isNotBlank(),
            onClick = {
                onAddSubReddit(subReddit)
            },
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = "Add SubReddit",
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(250.milliseconds)
        focusRequester.requestFocus()
    }
}
