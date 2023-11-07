package com.bidyut.tech.rewalled.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.bidyut.tech.rewalled.model.WallpaperId
import com.bidyut.tech.rewalled.ui.getSystemWidthPx
import com.bidyut.tech.rewalled.ui.theme.ReWalledTheme
import compose.icons.FeatherIcons
import compose.icons.feathericons.Download
import compose.icons.feathericons.ExternalLink
import compose.icons.feathericons.Maximize
import compose.icons.feathericons.Minimize
import compose.icons.feathericons.Share
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun WallpaperScreen(
    wallpaperId: WallpaperId,
    modifier: Modifier = Modifier,
    viewModel: SubRedditViewModel,
) {
    ReWalledTheme(
        isInDarkTheme = true,
    ) {
        val wallpaper = viewModel.getWallpaper(wallpaperId).collectAsState(initial = null)
        val screenWidthPx = getSystemWidthPx()
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
                                    FeatherIcons.Minimize,
                                    contentDescription = "Fit",
                                )
                            } else {
                                Icon(
                                    FeatherIcons.Maximize,
                                    contentDescription = "Fill",
                                )
                            }
                        }
                        IconButton(
                            modifier = Modifier.size(48.dp),
                            onClick = {
//                                wallpaper.value?.let { w ->
//                                    val sendIntent: Intent = Intent().apply {
//                                        action = Intent.ACTION_SEND
//                                        putExtra(Intent.EXTRA_TEXT, "${w.description} by ${w.author} at ${w.url}")
//                                        type = "text/plain"
//                                    }
//                                    context.startActivity(Intent.createChooser(sendIntent, "Share image with"))
//                                }
                            },
                        ) {
                            Icon(
                                FeatherIcons.Share,
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
                                FeatherIcons.ExternalLink,
                                contentDescription = "Open source",
                            )
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = {
//                            wallpaper.value?.let { w ->
//                                val downloadManager =
//                                    context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//                                val uri = Uri.parse(w.url)
//                                val request = DownloadManager.Request(uri)
//                                request.setTitle("${w.id} by ${w.author}")
//                                request.setDescription(w.description)
//                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.lastPathSegment)
//                                downloadManager.enqueue(request)
//                            }
                        }) {
                            Icon(
                                FeatherIcons.Download,
                                contentDescription = "Download",
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            wallpaper.value?.let {
                KamelImage(
                    resource = asyncPainterResource(it.getUriForSize(screenWidthPx)),
                    modifier = modifier.apply {
                        if (!isFullscreen) {
                            padding(paddingValues)
                        }
                    },
                    contentDescription = it.description,
                    contentScale = if (isFullscreen) {
                        ContentScale.Crop
                    } else {
                        ContentScale.Fit
                    },
                )
            }
        }
    }
}
