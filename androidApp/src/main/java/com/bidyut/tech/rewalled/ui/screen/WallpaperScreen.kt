@file:OptIn(ExperimentalMaterial3Api::class)

package com.bidyut.tech.rewalled.ui.screen

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.bidyut.tech.rewalled.model.WallpaperId
import com.bidyut.tech.rewalled.ui.theme.ReWalledTheme
import com.microsoft.fluent.mobile.icons.R

@Composable
fun WallpaperScreen(
    wallpaperId: WallpaperId,
    modifier: Modifier = Modifier,
    viewModel: SubRedditViewModel = viewModel(),
) {
    ReWalledTheme(
        darkTheme = true,
    ) {
        val wallpaper = viewModel.getWallpaper(wallpaperId).collectAsState(initial = null)
        val configuration = LocalConfiguration.current
        val screenWidthPx = with(LocalDensity.current) {
            configuration.screenWidthDp.dp.roundToPx()
        }
        var isFullscreen by remember {
            mutableStateOf(true)
        }
        val uriHandler = LocalUriHandler.current
        Scaffold(
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier.alpha(0.9f),
                    actions = {
                        IconButton(
                            modifier = Modifier.size(48.dp),
                            onClick = {
                                isFullscreen = !isFullscreen
                            },
                        ) {
                            if (isFullscreen) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_fluent_full_screen_minimize_24_regular),
                                    contentDescription = "Fit",
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_fluent_full_screen_maximize_24_regular),
                                    contentDescription = "Fill",
                                )
                            }
                        }
                        IconButton(
                            modifier = Modifier.size(48.dp),
                            onClick = { /*TODO*/ },
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_fluent_share_24_regular),
                                contentDescription = "Share",
                            )
                        }
                        IconButton(
                            modifier = Modifier.size(48.dp),
                            onClick = {
                                wallpaper.value?.let {
                                    uriHandler.openUri(it.postUrl)
                                }
                            },
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_fluent_open_24_regular),
                                contentDescription = "Open source",
                            )
                        }
                    },
                    floatingActionButton = {
                        val context = LocalContext.current
                        FloatingActionButton(onClick = {
                            wallpaper.value?.let { w ->
                                val downloadManager =
                                    context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                                val uri = Uri.parse(w.url)
                                val request = DownloadManager.Request(uri)
                                request.setTitle("${w.id} by ${w.author}")
                                request.setDescription(w.description)
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.lastPathSegment)
                                downloadManager.enqueue(request)
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_fluent_arrow_download_24_regular),
                                contentDescription = "Download",
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            AsyncImage(
                modifier = modifier.apply {
                    if (!isFullscreen) {
                        padding(paddingValues)
                    }
                },
                model = wallpaper.value?.getUriForSize(screenWidthPx),
                contentDescription = wallpaper.value?.description,
                contentScale = if (isFullscreen) {
                    ContentScale.Crop
                } else {
                    ContentScale.Fit
                },
            )
        }
    }
}
