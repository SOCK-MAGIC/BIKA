package com.shizq.bika.feature.comic.list

import androidx.compose.ui.util.fastJoinToString
import androidx.compose.ui.util.fastMap
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.NetworkComicList
import com.shizq.bika.core.network.model.Sort

class ComicListPagingSource(
    private val network: BikaNetworkDataSource,
    private val category: String,
) : PagingSource<Int, Comic>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Comic> {
        val nextPageNumber = params.key ?: 1
        val comicList = network.getComicList(
            sort = Sort.SORT_TIME_NEWEST,
            page = nextPageNumber,
            category = category
        )
        val comics = comicList.comics
        return try {
            LoadResult.Page(
                data = comics.docs.fastMap { it.mapToComic() },
                prevKey = null,
                nextKey = if (nextPageNumber < comics.pages) comics.page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Comic>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}

data class Comic(
    val id: String,
    val title: String,
    val finished: Boolean,
    val author: String,
    val like: Int,
    val total: Int,
    val categories: String,
    val thumbUrl: String
)

private fun NetworkComicList.Comics.Doc.mapToComic() = Comic(
    id,
    "$title${epsCount}E/${pagesCount}P",
    finished,
    author,
    likesCount,
    totalViews,
    categories.fastJoinToString("\t", prefix = "分类: "),
    thumb.imageUrl
)
