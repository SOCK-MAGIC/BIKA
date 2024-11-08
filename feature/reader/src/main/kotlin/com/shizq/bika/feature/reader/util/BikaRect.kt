package com.shizq.bika.feature.reader.util

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset

@Immutable
sealed class BikaRect(
    @Stable
    val left: Float,
    @Stable
    val top: Float,
    @Stable
    val right: Float,
    @Stable
    val bottom: Float,
) {
    data object TopStart : BikaRect(left = 0f, top = 0f, right = 0.33f, bottom = 0.33f)
    data object TopCenter : BikaRect(left = 0.33f, top = 0f, right = 0.66f, bottom = 0.33f)
    data object TopEnd : BikaRect(left = 0.66f, top = 0f, right = 1f, bottom = 0.33f)
    data object CenterStart : BikaRect(left = 0f, top = 0.33f, right = 0.33f, bottom = 0.66f)
    data object Center : BikaRect(left = 0.33f, top = 0.33f, right = 0.66f, bottom = 0.66f)
    data object CenterEnd : BikaRect(left = 0.66f, top = 0.33f, right = 1f, bottom = 0.66f)
    data object BottomStart : BikaRect(left = 0f, top = 0.66f, right = 0.33f, bottom = 1f)
    data object BottomCenter : BikaRect(left = 0.33f, top = 0.66f, right = 0.66f, bottom = 1f)
    data object BottomEnd : BikaRect(left = 0.66f, top = 0.66f, right = 1f, bottom = 1f)

    operator fun contains(offset: Offset): Boolean = offset.x >= left && offset.x < right && offset.y >= top && offset.y < bottom
}
