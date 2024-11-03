package com.shizq.bika.feature.comment

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.shizq.bika.core.data.model.Comment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PreviewCommentComponent :
    CommentComponent,
    ComponentContext by DefaultComponentContext(lifecycle = LifecycleRegistry()) {
    override val pagingDataFlow: Flow<PagingData<Comment>> = flowOf(PagingData.empty())
    override val childCommentPagingDataFlow: Flow<PagingData<Comment>> = flowOf(PagingData.empty())
    override fun updateCommentId(id: String) {}
}
