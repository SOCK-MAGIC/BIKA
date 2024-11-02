package com.shizq.bika.feature.comment

import androidx.paging.PagingData
import com.shizq.bika.core.data.model.Comments
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PreviewCommentComponent : CommentComponent {
    override val pagingDataFlow: Flow<PagingData<Comments>> = flowOf(PagingData.empty())
}
