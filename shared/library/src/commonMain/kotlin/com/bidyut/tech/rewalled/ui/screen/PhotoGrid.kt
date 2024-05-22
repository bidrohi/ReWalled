package com.bidyut.tech.rewalled.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bidyut.tech.rewalled.model.Wallpaper
import com.bidyut.tech.rewalled.ui.getSystemRatio
import com.bidyut.tech.rewalled.ui.getSystemWidthPx
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun WallpaperCard(
    wallpaper: Wallpaper,
    requestWidthPx: Int,
    imageRatio: Float,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
    ) {
        KamelImage(
            resource = asyncPainterResource(wallpaper.getUriForSize(requestWidthPx)),
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(imageRatio),
            contentDescription = wallpaper.description,
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun PhotoGrid(
    wallpapers: List<Wallpaper>,
    onWallpaperClick: (Wallpaper) -> Unit,
    contentPadding: PaddingValues,
    hasMore: Boolean,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val ratio = getSystemRatio()
    val screenWidthPx = getSystemWidthPx()
    LazyVerticalGrid(
        modifier = modifier,
        contentPadding = contentPadding,
        columns = GridCells.Adaptive(minSize = 128.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = wallpapers,
            key = { it.id },
        ) {
            WallpaperCard(
                wallpaper = it,
                requestWidthPx = screenWidthPx,
                imageRatio = ratio,
                modifier = Modifier.clickable { onWallpaperClick(it) }
            )
        }
        if (wallpapers.isNotEmpty() && hasMore) {
            item(
                span = { GridItemSpan(maxLineSpan) },
            ) {
                Button(
                    onClick = { onLoadMore() },
                ) {
                    Text("Load more")
                }
            }
        }
    }
}
