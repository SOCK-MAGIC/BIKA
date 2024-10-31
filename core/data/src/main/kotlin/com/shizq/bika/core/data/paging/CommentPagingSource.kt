package com.shizq.bika.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shizq.bika.core.data.model.PagingMetadata
import com.shizq.bika.core.data.util.asComicResource
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.Sort
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Inject

class CommentPagingSource @AssistedInject constructor(
    private val network: BikaNetworkDataSource,
    @Assisted private val comicId: String,
) : PagingSource<Int, String>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        val nextPageNumber = params.key ?: 1
        val comicList = network.getComments(comicId, nextPageNumber)

        // return try {
        //     LoadResult.Page(
        //         data = comicList.comics.asComicResource(),
        //         prevKey = if (page > 2) page - 1 else null,
        //         nextKey = if (nextPageNumber < pages) page + 1 else null,
        //     )
        // } catch (e: Exception) {
        //     LoadResult.Error(e)
        // }
        TODO()
    }

    override fun getRefreshKey(state: PagingState<Int, String>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    @AssistedFactory
    interface Factory {
        operator fun invoke(
            @Assisted comicId: String,
        ): CommentPagingSource
    }
}
