package com.bidyut.tech.rewalled.model

typealias WallpaperId = String

data class Wallpaper(
    val id: WallpaperId,
    val summary: String,
    val author: String,
    val url: String,
    val postUrl: String,
    val source: ImageDetail,
    val thumbnail: String,
    val resizedImages: List<ImageDetail>,
) {
    fun getUriForSize(width: Int): String {
        val url = resizedImages.filter {
            it.width <= width
        }.maxByOrNull {
            it.width
        }?.url ?: source.url
        return url.replace("&amp;", "&")
    }
}
