package com.bidyut.tech.rewalled.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeInput
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.blur.hazeBlur
import dev.chrisbanes.haze.blur.materials.HazeMaterials

@Composable
fun BottomBar(
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .safeDrawingPadding()
            .padding(16.dp)
            .clip(RoundedCornerShape(32.dp))
            .hazeBlur(
                input = HazeInput.Sources(hazeState),
                style = HazeMaterials.ultraThin(MaterialTheme.colorScheme.surfaceVariant),
            )
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        if (onBackClick != null) {
            IconButton(
                modifier = Modifier.padding(end = 8.dp)
                    .size(48.dp),
                onClick = onBackClick,
            ) {
                Icon(
                    Icons.AutoMirrored.TwoTone.ArrowBack,
                    contentDescription = "Back",
                )
            }
        }
        content()
    }
}
