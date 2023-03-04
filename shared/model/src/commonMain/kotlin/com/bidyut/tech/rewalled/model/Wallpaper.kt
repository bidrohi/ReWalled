package com.bidyut.tech.rewalled.model

data class Wallpaper(
    val id: String,
    val description: String,
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
