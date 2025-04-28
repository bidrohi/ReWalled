package com.bidyut.tech.rewalled.di

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bidyut.tech.rewalled.cache.Database
import com.bidyut.tech.rewalled.cache.DatabaseDriverFactory
import com.bidyut.tech.rewalled.model.Wallpaper
import com.bidyut.tech.rewalled.ui.PlatformContext
import com.bidyut.tech.rewalled.ui.PlatformCoordinator
import io.kamel.core.config.KamelConfig
import io.kamel.image.config.resourcesFetcher
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

class AndroidAppGraph(
    private val appCtx: Context,
    enableDebug: Boolean = false,
) : AppGraph() {

    override val database by lazy {
        Database(DatabaseDriverFactory(appCtx))
    }

    override val httpClient by lazy {
        HttpClient(OkHttp) {
            baseConfiguration(enableDebug)
        }
    }

    override val kamelConfig by lazy {
        KamelConfig {
            baseConfiguration(enableDebug)
            resourcesFetcher(appCtx)
        }
    }

    override val coordinator by lazy {
        object : PlatformCoordinator {
            override fun triggerShareIntent(
                context: PlatformContext,
                w: Wallpaper,
            ) {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "${w.summary} by ${w.author} at ${w.url}")
                    type = "text/plain"
                }
                ContextCompat.startActivity(
                    context.context,
                    Intent.createChooser(sendIntent, "Share wallpaper with"),
                    null,
                )
            }

            override fun triggerDownloadIntent(
                context: PlatformContext,
                w: Wallpaper,
            ) {
                val downloadManager = context.context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                val uri = w.url.toUri()
                val request = DownloadManager.Request(uri)
                request.setTitle(w.summary)
                request.setDescription("${w.id} by ${w.author}")
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    request.setDestinationInExternalFilesDir(context.context, Environment.DIRECTORY_DOWNLOADS, uri.lastPathSegment)
                } else {
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.lastPathSegment)
                }
                downloadManager.enqueue(request)
            }
        }
    }
}
