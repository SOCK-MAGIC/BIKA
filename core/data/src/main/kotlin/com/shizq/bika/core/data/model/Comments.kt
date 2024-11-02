package com.shizq.bika.core.data.model

import com.shizq.bika.core.network.model.NetworkComment

data class Comments(
    val id: String,
    val comicId: String,
    val content: String,
    val createdAt: String,
    val subComment: Int,
    val likesCount: Int,
    val isLike: Boolean,
)

internal fun NetworkComment.Comments.Doc.asComment() = Comments(
    id = id,
    comicId = comic,
    content = content,
    createdAt = createdAt,
    subComment = totalComments,
    likesCount = likesCount,
    isLike = isLiked,
)

internal fun NetworkComment.asCommentList(): List<Comments> = comments.docs.map { it.asComment() } + topComments.map { it.asComment() }