package com.shizq.bika.feature.comic.list

import androidx.compose.ui.util.fastMap
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.NetworkComicList
import com.shizq.bika.core.network.model.Sort

class ComicListPagingSource(
    private val network: BikaNetworkDataSource,
    private val category: String,
) : PagingSource<Int, ComicResource>() {
    override suspend fun load(
        params: LoadParams<Int>,
    ): LoadResult<Int, ComicResource> {
        val nextPageNumber = params.key ?: 1
        val comicList = network.getComicList(
            sort = Sort.SORT_TIME_NEWEST,
            page = nextPageNumber,
            category = category,
        )
        val comics = comicList.comics
        return try {
            LoadResult.Page(
                data = comics.docs.fastMap { it.asComicResource() },
                prevKey = null,
                nextKey = if (nextPageNumber < comics.pages) comics.page + 1 else null,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ComicResource>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
}

fun NetworkComicList.Comics.Doc.asComicResource() = ComicResource(
    id,
    thumb.imageUrl,
    title,
    author,
    categories,
    finished,
    epsCount,
    pagesCount,
    likesCount,
)

data class Comic(
    val id: String,
    val title: String,
    val finished: Boolean,
    val author: String,
    val likesCount: Int,
    val total: Int,
    val categories: String,
    val thumbUrl: String,
    val epsCount: Int,
    val pagesCount: Int,
)
