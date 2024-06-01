package com.bidyut.tech.rewalled.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bidyut.tech.rewalled.model.Wallpaper
import com.bidyut.tech.rewalled.ui.getSystemRatio
import com.bidyut.tech.rewalled.ui.getSystemWidth
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun WallpaperCard(
    wallpaper: Wallpaper,
    requestWidth: Dp,
    imageRatio: Float,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
) {
    val requestWidthPx = with(LocalDensity.current) {
        requestWidth.roundToPx()
    }
    KamelImage(
        resource = { asyncPainterResource(wallpaper.getUriForSize(requestWidthPx)) },
        modifier = modifier.aspectRatio(imageRatio)
            .clip(RoundedCornerShape(cornerRadius)),
        contentDescription = wallpaper.summary,
        contentScale = ContentScale.Crop,
    )
}

@Composable
fun PhotoGrid(
    wallpapers: List<Wallpaper>,
    onWallpaperClick: (Wallpaper) -> Unit,
    contentPadding: PaddingValues,
    afterCursor: String?,
    onLoadMore: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val ratio = getSystemRatio()
    val screenWidth = getSystemWidth()
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
                requestWidth = screenWidth,
                imageRatio = ratio,
                modifier = Modifier.clickable { onWallpaperClick(it) }
            )
        }
        if (wallpapers.isNotEmpty() && afterCursor?.isNotEmpty() == true) {
            item {
                LaunchedEffect(afterCursor) {
                    onLoadMore(afterCursor)
                }
            }
        }
    }
}
