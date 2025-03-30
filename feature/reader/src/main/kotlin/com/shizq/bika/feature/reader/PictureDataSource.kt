package com.shizq.bika.feature.reader

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListPrefetchStrategy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.snapshotFlow
import com.shizq.bika.core.data.model.PagingMetadata
import com.shizq.bika.core.data.paging.ReaderPagingSource
import com.shizq.bika.core.model.Picture
import com.shizq.bika.core.network.BikaNetworkDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

interface PictureDataSource {
    val metadata: StateFlow<PagingMetadata>
    val pictures: StateFlow<List<Picture>>
}

class PictureDataSourceImpl @AssistedInject constructor(
    @Assisted private val id: String,
    @Assisted private val order: Int,
    private val network: BikaNetworkDataSource,
) : PictureDataSource {
    override val pictures: MutableStateFlow<List<Picture>> = MutableStateFlow(emptyList())
    override val metadata = MutableStateFlow(PagingMetadata(0, 0, 0))

    @OptIn(ExperimentalFoundationApi::class)
    val lazyListState =
        LazyListState(prefetchStrategy = LazyListPrefetchStrategy(NESTED_PREFETCH_ITEM_COUNT))
    private var page = 0
    val s = snapshotFlow { lazyListState.isLastItemVisible(5) }
        .filter { it }
        .map {
            network.getComicEpPictures(
                id = id,
                epOrder = order,
                page = ++page,
            )
        }
        .catch {
            page--
        }
        .onEach { epPicture ->
            metadata.update {
                PagingMetadata(epPicture.pages.pages, epPicture.pages.page, epPicture.pages.total)
            }
        }.map { it.pages.docs.map { doc -> Picture(doc.id, doc.media.imageUrl) } }

    @AssistedFactory
    interface Factory {
        operator fun invoke(
            id: String,
            order: Int,
        ): ReaderPagingSource
    }
}

fun LazyListState.isLastItemVisible(index: Int = 1): Boolean =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - index

val LazyListState.isFirstItemVisible: Boolean
    get() = firstVisibleItemIndex == 0

private const val NESTED_PREFETCH_ITEM_COUNT = 5