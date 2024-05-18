package com.bidyut.tech.rewalled.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.rememberWindowState
import com.bidyut.tech.rewalled.model.Wallpaper
import kotlin.math.roundToInt

@Composable
actual fun getCurrentContext(): Any = Unit

@Composable
actual fun getSystemWidthPx(): Int {
    val windowState = rememberWindowState()
    return windowState.size.width.value.roundToInt()
}

@Composable
actual fun getSystemRatio(): Float {
    val windowState = rememberWindowState()
    return windowState.size.width.value / windowState.size.height.value
}

actual fun triggerShareIntent(
    context: Any,
    w: Wallpaper,
) {
    // TODO:
//    val sendIntent: Intent = Intent().apply {
//        action = Intent.ACTION_SEND
//        putExtra(Intent.EXTRA_TEXT, "${w.description} by ${w.author} at ${w.url}")
//        type = "text/plain"
//    }
//    (context as Context)
//        .startActivity(Intent.createChooser(sendIntent, "Share image with"))
}

actual fun triggerDownloadIntent(
    context: Any,
    w: Wallpaper,
) {
    // TODO:
//    val downloadManager = (context as Context)
//        .getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//    val uri = Uri.parse(w.url)
//    val request = DownloadManager.Request(uri)
//    request.setTitle("${w.id} by ${w.author}")
//    request.setDescription(w.description)
//    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.lastPathSegment)
//    downloadManager.enqueue(request)
}
