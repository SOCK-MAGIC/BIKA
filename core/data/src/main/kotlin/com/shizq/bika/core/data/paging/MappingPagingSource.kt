package com.shizq.bika.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState

internal class MappingPagingSource<Key : Any, Value : Any, MappedValue : Any>(
    private val originalSource: PagingSource<Key, Value>,
    private val mapper: (Value) -> MappedValue,
) : PagingSource<Key, MappedValue>() {

    override fun getRefreshKey(state: PagingState<Key, MappedValue>): Key? = originalSource.getRefreshKey(
        PagingState(
            pages = emptyList(),
            leadingPlaceholderCount = 0,
            anchorPosition = state.anchorPosition,
            config = state.config,
        ),
    )

    override suspend fun load(params: LoadParams<Key>): LoadResult<Key, MappedValue> = when (val originalResult = originalSource.load(params)) {
        is LoadResult.Error -> LoadResult.Error(originalResult.throwable)
        is LoadResult.Invalid -> LoadResult.Invalid()
        is LoadResult.Page -> LoadResult.Page(
            data = originalResult.data.map(mapper),
            prevKey = originalResult.prevKey,
            nextKey = originalResult.nextKey,
        )
    }

    override val jumpingSupported: Boolean
        get() = originalSource.jumpingSupported
}
