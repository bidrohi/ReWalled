package com.bidyut.tech.rewalled.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bidyut.tech.rewalled.ui.Route
import com.bidyut.tech.rewalled.ui.theme.ReWalled
import com.bidyut.tech.rewalled.ui.theme.ReWalledTheme

@Composable
fun SettingsScreen(
    navigator: NavController,
    modifier: Modifier = Modifier
) {
    ReWalledTheme {
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
                        Text("About")
                    }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier.fillMaxSize(),
            ) {
                item("logo") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            imageVector = ReWalled,
                            contentDescription = "ReWalled Logo",
                            modifier = Modifier.size(128.dp),
                        )
                        Column {
                            Text(
                                text = "ReWalled",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.secondary,
                                            MaterialTheme.colorScheme.tertiary,
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
                item("categories") {
                    Text(
                        text = "Configure categories",
                        modifier = Modifier.fillMaxWidth()
                            .clickable {
                                navigator.navigate(Route.SettingsCategories.uri)
                            }
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}
