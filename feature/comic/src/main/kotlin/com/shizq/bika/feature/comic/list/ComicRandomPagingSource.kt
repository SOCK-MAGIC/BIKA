package com.shizq.bika.feature.comic.list

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.NetworkComicSimple
import com.shizq.bika.feature.comic.model.asComicResource

class ComicRandomPagingSource(
    private val network: BikaNetworkDataSource,
) : PagingSource<Int, ComicResource>() {
    override suspend fun load(
        params: LoadParams<Int>,
    ): LoadResult<Int, ComicResource> {
        val comicList = network.comicsRandom().comics.map { it.asComicResource() }

        return try {
            LoadResult.Page(
                data = comicList,
                prevKey = null,
                nextKey = null,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ComicResource>): Int? = null
}

fun NetworkComicSimple.asComicResource(): ComicResource = ComicResource(
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
