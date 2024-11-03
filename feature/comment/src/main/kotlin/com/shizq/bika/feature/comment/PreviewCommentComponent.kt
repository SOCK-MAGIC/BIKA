package com.shizq.bika.feature.comment

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.shizq.bika.core.data.model.Comment
import com.shizq.bika.core.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PreviewCommentComponent :
    CommentComponent,
    ComponentContext by DefaultComponentContext(lifecycle = LifecycleRegistry()) {
    private val list = MutableList(20) {
        Comment(
            id = it.toString(),
            comicId = it.toString(),
            content = LoremIpsum5Word().values.joinToString(""),
            createdAt = System.currentTimeMillis().toString(),
            subComment = it,
            likesCount = it,
            isLike = true,
            user = User(
                "",
                "",
                emptyList(),
                0,
                "",
                "",
                0,
                it.toString(),
                "",
                "",
                it.toString(),
            ),
        )
    }
    override val pagingDataFlow: Flow<PagingData<Comment>> = flowOf(PagingData.from(list))
    override val childCommentPagingDataFlow: Flow<PagingData<Comment>> =
        flowOf(PagingData.from(list))

    override fun updateCommentId(id: String) {}
}

class LoremIpsum5Word : LoremIpsum(5)
