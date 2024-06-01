package com.bidyut.tech.rewalled.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ReWalled: ImageVector
    get() {
        if (_reWalled != null) {
            return _reWalled!!
        }
        _reWalled = Builder(name = "Rewalled", defaultWidth = 48.0.dp, defaultHeight = 48.0.dp,
                viewportWidth = 450.0f, viewportHeight = 450.0f).apply {
            path(fill = SolidColor(Color(0xFFE8EDED)), stroke = SolidColor(Color(0x00000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(89.9f, 137.7f)
                curveToRelative(0.0f, 0.5f, 0.0f, 4.4f, 0.1f, 8.8f)
                curveToRelative(0.1f, 4.4f, 0.5f, 37.2f, 0.9f, 73.0f)
                lineToRelative(0.7f, 65.0f)
                lineToRelative(2.6f, 6.7f)
                curveToRelative(3.5f, 9.3f, 7.8f, 15.6f, 15.8f, 23.2f)
                curveToRelative(17.9f, 16.8f, 43.2f, 21.1f, 64.4f, 10.8f)
                curveToRelative(8.8f, -4.3f, 20.1f, -13.6f, 19.2f, -15.8f)
                curveToRelative(-0.3f, -0.8f, 0.0f, -1.3f, 0.7f, -1.1f)
                curveToRelative(0.7f, 0.1f, 1.1f, -0.6f, 1.1f, -1.5f)
                curveToRelative(-0.1f, -1.0f, 0.2f, -1.6f, 0.6f, -1.3f)
                curveToRelative(1.1f, 0.7f, 3.3f, -2.9f, 6.1f, -10.0f)
                curveToRelative(2.3f, -6.0f, 2.4f, -6.3f, 2.9f, -54.2f)
                lineToRelative(0.5f, -48.1f)
                lineToRelative(3.0f, -4.4f)
                curveToRelative(8.3f, -11.8f, 25.1f, -11.6f, 36.2f, 0.5f)
                curveToRelative(5.8f, 6.3f, 7.5f, 10.7f, 7.7f, 19.7f)
                curveToRelative(0.1f, 6.3f, -0.4f, 8.4f, -2.7f, 13.2f)
                curveToRelative(-4.7f, 9.5f, -14.1f, 15.8f, -23.8f, 15.8f)
                curveToRelative(-7.0f, -0.0f, -6.9f, -0.2f, -6.9f, 14.5f)
                curveToRelative(0.0f, 16.1f, 1.1f, 22.4f, 5.1f, 29.0f)
                curveToRelative(4.0f, 6.7f, 31.0f, 34.3f, 37.9f, 38.9f)
                curveToRelative(16.5f, 11.0f, 38.4f, 13.8f, 57.2f, 7.5f)
                curveToRelative(18.9f, -6.3f, 34.9f, -23.2f, 40.3f, -42.4f)
                curveToRelative(1.5f, -5.3f, 1.8f, -14.6f, 2.2f, -77.3f)
                lineToRelative(0.5f, -71.2f)
                lineToRelative(-2.8f, -0.0f)
                curveToRelative(-10.8f, 0.1f, -26.0f, 3.9f, -30.5f, 7.7f)
                curveToRelative(-6.2f, 5.3f, -6.1f, 3.7f, -6.0f, 69.5f)
                curveToRelative(0.1f, 40.0f, -0.2f, 61.1f, -0.9f, 63.8f)
                curveToRelative(-2.3f, 8.6f, -12.6f, 16.0f, -22.4f, 16.0f)
                curveToRelative(-9.8f, -0.0f, -12.1f, -1.3f, -26.1f, -15.3f)
                lineToRelative(-12.9f, -13.0f)
                lineToRelative(2.4f, -1.6f)
                curveToRelative(20.1f, -13.1f, 29.8f, -32.2f, 28.7f, -56.6f)
                curveToRelative(-0.8f, -19.4f, -7.2f, -34.1f, -20.8f, -47.5f)
                curveToRelative(-13.0f, -12.9f, -26.2f, -18.4f, -44.4f, -18.3f)
                curveToRelative(-26.8f, 0.1f, -49.4f, 14.9f, -58.1f, 37.9f)
                curveToRelative(-1.6f, 4.4f, -1.8f, 9.7f, -2.3f, 50.9f)
                curveToRelative(-0.7f, 51.2f, -0.4f, 49.6f, -7.7f, 55.2f)
                curveToRelative(-3.0f, 2.2f, -4.7f, 2.7f, -9.3f, 2.8f)
                curveToRelative(-7.3f, -0.0f, -12.9f, -3.4f, -16.0f, -9.7f)
                curveToRelative(-2.1f, -4.4f, -2.1f, -5.4f, -2.1f, -61.7f)
                curveToRelative(0.0f, -41.1f, -0.3f, -58.5f, -1.2f, -61.3f)
                curveToRelative(-1.6f, -5.3f, -7.9f, -12.2f, -13.0f, -14.3f)
                curveToRelative(-6.7f, -2.7f, -26.7f, -5.5f, -26.9f, -3.8f)
                close()
                moveTo(92.7f, 278.7f)
                curveToRelative(-0.4f, 0.3f, -0.7f, -0.0f, -0.7f, -0.7f)
                curveToRelative(0.0f, -0.7f, 0.3f, -1.0f, 0.7f, -0.7f)
                curveToRelative(0.3f, 0.4f, 0.3f, 1.0f, 0.0f, 1.4f)
                close()
            }
        }
        .build()
        return _reWalled!!
    }

private var _reWalled: ImageVector? = null
