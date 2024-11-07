package com.shizq.bika.core.ui

data class ComicCardSharedElementKey(
    val comicId: String,
    val type: ComicCardSharedElementType,
)

enum class ComicCardSharedElementType {
    Bounds,
    Image,
    Title,
    Tagline,
    Author
}
