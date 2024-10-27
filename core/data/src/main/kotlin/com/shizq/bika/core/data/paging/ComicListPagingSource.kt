package com.shizq.bika.core.data.paging

import com.shizq.bika.core.data.model.PagingMetadata
import com.shizq.bika.core.data.util.asComicResource
import com.shizq.bika.core.datastore.BikaInterestsDataSource
import com.shizq.bika.core.model.ComicResource
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.Comics
import com.shizq.bika.core.network.model.Sort
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.first

class ComicListPagingSource @AssistedInject constructor(
    private val network: BikaNetworkDataSource,
    private val userInterests: BikaInterestsDataSource,
    @Assisted private val sort: Sort,
    @Assisted private val comics: Comics,
    @Assisted private val page: Int?,
    @Assisted private val pagingMetadata: (PagingMetadata) -> Unit,
) : BikaComicListPagingSource() {
    override val jumpingSupported = true
    override val keyReuseSupported = true
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ComicResource> {
        Napier.i(tag = "ComicListPagingSource") { "$comics ${sort.name} $page" }
        val hide = userInterests.userHideCategories.first()
        Napier.i(tag = "ComicListPagingSource") { "filter category: $hide" }
        // 当分类与主页面相同时会造成无限加载
        if (hide.contains(comics.category)) return LoadResult.Invalid()
        val nextPageNumber = page ?: params.key ?: 1
        val originalComicList = network.getComicList(
            sort = sort,
            page = nextPageNumber,
            comics = comics,
        )
        val (docs, limit, page, pages, total) = originalComicList.comics
        pagingMetadata(PagingMetadata(total, page, pages))
        val comicList = docs.map { it.asComicResource() }

        return try {
            LoadResult.Page(
                data = comicList,
                prevKey = if (page > 2) page - 1 else null,
                nextKey = if (nextPageNumber < pages) page + 1 else null,
                itemsBefore = page * limit,
                itemsAfter = (pages - page) * limit,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    @AssistedFactory
    interface Factory {
        operator fun invoke(
            @Assisted sort: Sort,
            @Assisted comics: Comics,
            @Assisted page: Int?,
            @Assisted pagingMetadata: (PagingMetadata) -> Unit,
        ): ComicListPagingSource
    }
}
