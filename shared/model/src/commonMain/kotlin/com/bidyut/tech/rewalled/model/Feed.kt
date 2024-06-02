package com.bidyut.tech.rewalled.model

interface Feed {
    val idStr: String
    val wallpapers: List<Wallpaper>
    val afterCursor: String?
}
