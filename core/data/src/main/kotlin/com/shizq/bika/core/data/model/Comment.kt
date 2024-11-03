package com.shizq.bika.core.data.model

import com.shizq.bika.core.network.model.ChildComment
import com.shizq.bika.core.network.model.CommentDoc
import com.shizq.bika.core.network.model.NetworkComment
import kotlinx.datetime.Instant
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char

data class Comment(
    val id: String,
    val comicId: String,
    val content: String,
    val createdAt: String,
    val subComment: Int,
    val likesCount: Int,
    val isLike: Boolean,
    val user: User,
)

data class User(
    val avatarUrl: String,
    val character: String,
    val characters: List<String>,
    val exp: Int,
    val gender: String,
    val id: String,
    val level: Int,
    val name: String,
    val role: String,
    val slogan: String,
    val title: String,
)

internal fun CommentDoc.asComment() = Comment(
    id = id,
    comicId = comic,
    content = content,
    createdAt = Instant.parse(createdAt).format(dateFormat),
    subComment = totalComments,
    likesCount = likesCount,
    isLike = isLiked,
    user = User(
        user.avatar.imageUrl,
        user.character,
        user.characters,
        user.exp,
        user.gender,
        user.id,
        user.level,
        user.name,
        user.role,
        user.slogan,
        user.title,
    ),
)

internal fun NetworkComment.asCommentList(): List<Comment> {
    val top = if (comments.page == "1") {
        topComments.map { top -> top.asComment() }
    } else {
        emptyList()
    }
    return top + comments.docs.map { it.asComment() }
}

internal fun ChildComment.asCommentList(): List<Comment> = comments.docs.map { it.asComment() }

private val dateFormat = DateTimeComponents.Format {
    year()
    chars("年")
    monthNumber()
    chars("月")
    dayOfMonth()
    chars("日")
    char(' ')
    hour()
    char(':')
    minute()
}
