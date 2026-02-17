package com.bidyut.tech.rewalled.ui.screen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bidyut.tech.rewalled.ui.CONTENT_ANIMATION_DURATION
import com.bidyut.tech.rewalled.ui.Route
import com.bidyut.tech.rewalled.ui.components.BottomBar
import com.bidyut.tech.rewalled.ui.theme.ReWalled
import com.bidyut.tech.rewalled.ui.theme.ReWalledTheme
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.rememberHazeState

@Composable
fun SharedTransitionScope.SettingsScreen(
    navigator: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    ReWalledTheme {
        val hazeState = rememberHazeState()
        Scaffold(
            modifier = modifier,
            bottomBar = {
                BottomBar(
                    hazeState = hazeState,
                    onBackClick = navigator::popBackStack,
                ) {
                    Text(
                        text = "About",
                        modifier = Modifier.padding(end = 32.dp),
                    )
                }
            }
        ) { paddingValues ->
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier.fillMaxSize()
                    .hazeSource(hazeState),
            ) {
                item("logo") {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Row(
                            modifier = Modifier.background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = MaterialTheme.shapes.extraLarge
                            ).sharedElement(
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ ->
                                    tween(durationMillis = CONTENT_ANIMATION_DURATION)
                                },
                                sharedContentState = rememberSharedContentState(
                                    key = "image-logo"
                                ),
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Icon(
                                imageVector = ReWalled,
                                contentDescription = "ReWalled Logo",
                                modifier = Modifier.size(128.dp),
                            )
                            Column(
                                modifier = Modifier.padding(end = 32.dp),
                            ) {
                                Text(
                                    text = "ReWalled",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.primaryFixedDim,
                                                MaterialTheme.colorScheme.secondaryFixedDim,
                                                MaterialTheme.colorScheme.tertiaryFixedDim,
                                            ),
                                        )
                                    ),
                                )
                                Text(
                                    text = "@bidyut",
                                    style = MaterialTheme.typography.titleSmall,
                                )
                            }
                        }
                    }
                }
                item("categories") {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .clickable {
                                navigator.navigate(Route.SettingsCategories)
                            }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "Configure categories",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.sharedElement(
                                animatedVisibilityScope = animatedVisibilityScope,
                                boundsTransform = { _, _ ->
                                    tween(durationMillis = CONTENT_ANIMATION_DURATION)
                                },
                                sharedContentState = rememberSharedContentState(
                                    key = "categories-config-title"
                                ),
                            ),
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.NavigateNext,
                            contentDescription = null,
                        )
                    }
                }
            }
        }
    }
}
