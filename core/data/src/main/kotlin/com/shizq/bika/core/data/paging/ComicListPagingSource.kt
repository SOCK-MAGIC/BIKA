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

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ComicResource> {
        val hide = userInterests.userHideCategories.first()
        Napier.i(tag = "ComicListPagingSource") { "filter category: $hide" }
        Napier.i(tag = "ComicListPagingSource") { "page: " + page.toString() }
        // todo 当分类 与 主页面 相同时会造成无线加载
        // 例如 hide 中有 cos 与 category=“cos”
        if (hide.contains(comics.category)) return LoadResult.Invalid()
        val nextPageNumber = params.key ?: 1
        val originalComicList = network.getComicList(
            sort = sort,
            page = nextPageNumber,
            comics = comics,
        )

        val page = originalComicList.comics.page
        val pages = originalComicList.comics.pages
        pagingMetadata(PagingMetadata(originalComicList.comics.total, page, pages))
        val comics = originalComicList.comics
        val comicList = comics.docs.filter { doc ->
            for (c in doc.categories) {
                if (hide.contains(c)) {
                    return@filter false
                }
            }
            return@filter true
        }.map { it.asComicResource() }

        return try {
            LoadResult.Page(
                data = comicList,
                prevKey = if (page > 2) page - 1 else null,
                nextKey = if (nextPageNumber < pages) page + 1 else null,
                itemsBefore = 0,
                itemsAfter = 0,
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
            @Assisted page: Int? = null,
            @Assisted pagingMetadata: (PagingMetadata) -> Unit,
        ): ComicListPagingSource
    }
}
