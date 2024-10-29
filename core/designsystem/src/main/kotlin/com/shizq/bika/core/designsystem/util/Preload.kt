package com.shizq.bika.core.designsystem.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import coil3.ImageLoader
import coil3.imageLoader
import coil3.request.ImageRequest
import kotlin.math.max
import kotlin.math.min

@Composable
public fun <DataT : Any> rememberCoilPreloadingData(
    dataSize: Int,
    dataGetter: (Int) -> DataT,
    fixedVisibleItemCount: Int? = null,
    request: ImageRequest.Builder.(item: DataT) -> ImageRequest,
): PreloadingData<DataT> {
    val context = LocalContext.current

    return remember(
        context,
        dataSize,
        dataGetter,
        fixedVisibleItemCount,
        request,
    ) {
        val preloaderData = PreloaderData(dataGetter, request, context)
        val preloader =
            ListPreloader(context.imageLoader, PreloadModelProvider(preloaderData), 10)
        PreloadDataImpl(
            size = dataSize,
            indexToData = dataGetter,
            fixedVisibleItemCount = fixedVisibleItemCount,
            request = request,
            preloader = preloader,
        )
    }
}

@Composable
fun <T : Any> rememberCoilPreloadingData(
    data: List<T>,
    fixedVisibleItemCount: Int? = null,
    request: ImageRequest.Builder.(item: T) -> ImageRequest,
): PreloadingData<T> {
    return rememberCoilPreloadingData(
        data.size,
        data::get,
        fixedVisibleItemCount,
        request,
    )
}

public interface PreloadingData<DataT> {
    /** The total number of items in the data set. */
    public val size: Int

    @Composable
    public operator fun get(index: Int): Pair<DataT, ImageRequest>
}

private class PreloadDataImpl<DataT : Any>(
    override val size: Int,
    private val indexToData: (Int) -> DataT,
    private val fixedVisibleItemCount: Int?,
    private val request: ImageRequest.Builder.(item: DataT) -> ImageRequest,
    private val preloader: ListPreloader<DataT>,
) : PreloadingData<DataT> {
    @Composable
    override fun get(index: Int): Pair<DataT, ImageRequest> {
        val item = indexToData(index)
        LaunchedEffect(request, fixedVisibleItemCount, index) {
            preloader.onScroll(
                index,
                fixedVisibleItemCount ?: 1,
                size,
            )
        }
        return item to ImageRequest.Builder(LocalContext.current).request(item)
    }
}

private data class PreloaderData<DataT>(
    val dataAccessor: (Int) -> DataT,
    val request: ImageRequest.Builder.(item: DataT) -> ImageRequest,
    val context: Context,
) {
    fun preloadRequests(item: DataT): ImageRequest {
        return ImageRequest.Builder(context).request(item)
    }
}

private class PreloadModelProvider<DataT : Any>(
    private val data: PreloaderData<DataT>,
) : ListPreloader.PreloadModelProvider<DataT> {

    override fun getPreloadItems(position: Int): MutableList<DataT> {
        return mutableListOf(data.dataAccessor(position))
    }

    override fun getPreloadRequest(item: DataT): ImageRequest {
        return data.preloadRequests(item)
    }
}

/**
 * @see com.bumptech.glide.ListPreloader
 */
class ListPreloader<T>(
    private val imageLoader: ImageLoader,
    private val preloadModelProvider: PreloadModelProvider<T>,
    private val maxPreload: Int,
) {
    private var lastEnd = 0
    private var lastStart = 0
    private var lastFirstVisible = -1
    private var totalItemCount = 0

    private var isIncreasing = true

    interface PreloadModelProvider<U> {
        fun getPreloadItems(position: Int): List<U>
        fun getPreloadRequest(item: U): ImageRequest
    }

    fun onScroll(firstVisible: Int, visibleCount: Int, totalCount: Int) {
        if (totalItemCount == 0 && totalCount == 0) {
            return
        }
        totalItemCount = totalCount
        if (firstVisible > lastFirstVisible) {
            preload(firstVisible + visibleCount, true)
        } else if (firstVisible < lastFirstVisible) {
            preload(firstVisible, false)
        }
        lastFirstVisible = firstVisible
    }

    private fun preload(start: Int, increasing: Boolean) {
        if (isIncreasing != increasing) {
            isIncreasing = increasing
        }
        preload(start, start + (if (increasing) maxPreload else -maxPreload))
    }

    private fun preload(from: Int, to: Int) {
        var start: Int
        var end: Int
        if (from < to) {
            start = max(lastEnd, from)
            end = to
        } else {
            start = to
            end = min(lastStart, from)
        }
        end = min(totalItemCount, end)
        start = min(totalItemCount, max(0, start))

        if (from < to) {
            // Increasing
            for (i in start until end) {
                preloadAdapterPosition(preloadModelProvider.getPreloadItems(i), true)
            }
        } else {
            // Decreasing
            for (i in end - 1 downTo start) {
                preloadAdapterPosition(preloadModelProvider.getPreloadItems(i), false)
            }
        }

        lastStart = start
        lastEnd = end
    }

    private fun preloadAdapterPosition(items: List<T>, isIncreasing: Boolean) {
        val numItems = items.size
        if (isIncreasing) {
            for (i in 0 until numItems) {
                preloadItem(items[i])
            }
        } else {
            for (i in numItems - 1 downTo 0) {
                preloadItem(items[i])
            }
        }
    }

    private fun preloadItem(item: T) {
        if (item == null) {
            return
        }
        val imageRequest = preloadModelProvider.getPreloadRequest(item)
        imageLoader.enqueue(imageRequest)
    }
}
