package com.shizq.bika.feature.comic

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.Sort

// class ComicListPagingSource(
//     private val network: BikaNetworkDataSource,
//     private val category: String,
// ) : PagingSource<Int, String>() {
//     override suspend fun load(
//         params: LoadParams<Int>
//     ): LoadResult<Int, String> {
//         return try {
//             LoadResult.Page(
//                 data = response.users,
//                 prevKey = null, // Only paging forward.
//                 nextKey = response.nextPageNumber
//             )
//         } catch (e: Exception) {
//             LoadResult.Error(e)
//         }
//     }
//
//     override fun getRefreshKey(state: PagingState<Int, User>): Int? {
//         // Try to find the page key of the closest page to anchorPosition from
//         // either the prevKey or the nextKey; you need to handle nullability
//         // here.
//         //  * prevKey == null -> anchorPage is the first page.
//         //  * nextKey == null -> anchorPage is the last page.
//         //  * both prevKey and nextKey are null -> anchorPage is the
//         //    initial page, so return null.
//         return state.anchorPosition?.let { anchorPosition ->
//             val anchorPage = state.closestPageToPosition(anchorPosition)
//             anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//         }
//     }
// }