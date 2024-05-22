package com.bidyut.tech.rewalled.ui

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.bidyut.tech.rewalled.model.Wallpaper

@Composable
actual fun getCurrentContext(): Any = LocalContext.current

@Composable
actual fun getSystemWidthPx(): Int {
    val configuration = LocalConfiguration.current
    return with(LocalDensity.current) {
        configuration.screenWidthDp.dp.roundToPx()
    }
}

@Composable
actual fun getSystemRatio(): Float {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp.toFloat() / configuration.screenHeightDp
}

actual fun triggerShareIntent(
    context: Any,
    w: Wallpaper,
) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "${w.description} by ${w.author} at ${w.url}")
        type = "text/plain"
    }
    (context as Context)
        .startActivity(Intent.createChooser(sendIntent, "Share image with"))
}

actual fun triggerDownloadIntent(
    context: Any,
    w: Wallpaper,
) {
    val downloadManager = (context as Context)
        .getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val uri = Uri.parse(w.url)
    val request = DownloadManager.Request(uri)
    request.setTitle(w.description)
    request.setDescription("${w.id} by ${w.author}")
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.lastPathSegment)
    downloadManager.enqueue(request)
}
