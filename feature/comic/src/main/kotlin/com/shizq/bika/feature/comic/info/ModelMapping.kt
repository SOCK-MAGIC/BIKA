package com.shizq.bika.feature.comic.info

import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.model.NetworkComicEp
import com.shizq.bika.core.network.model.NetworkComicInfo
import com.shizq.bika.core.ui.UserInfo

internal fun NetworkComicInfo.asToolItems(): ToolItem = ToolItem(
    allowComment = comic.allowComment,
    allowDownload = comic.allowDownload,
    commentsCount = comic.commentsCount,
    isFavourite = comic.isFavourite,
    isLiked = comic.isLiked,
    totalLikes = comic.totalLikes,
    pagesCount = comic.pagesCount,
    epsCount = comic.epsCount,
)

internal fun NetworkComicInfo.asComicResource(): ComicResource = ComicResource(
    comic.id,
    comic.thumb.imageUrl,
    comic.title,
    comic.author,
    comic.categories,
    comic.finished,
    comic.epsCount,
    comic.pagesCount,
    comic.likesCount,
)

internal fun NetworkComicInfo.asCreator(): UserInfo {
    val creator = comic.creator
    return UserInfo(
        avatarUrl = creator.avatar.staticImageUrl,
        character = creator.character,
        gender = creator.gender,
        level = creator.level.toString(),
        name = creator.name,
        slogan = creator.slogan,
        title = creator.title,
        id = creator.id,
    )
}

internal fun NetworkComicEp.Eps.Doc.asEpDoc() =
    EpDoc(id, order, title, updatedAt)

data class EpDoc(
    val id: String,
    val order: Int,
    val title: String,
    val updatedAt: String,
)
