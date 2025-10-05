package com.shizq.bika.feature.reader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.shizq.bika.core.data.paging.ReaderPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
//    id: String,
//    order: Int,
    readerPagingSourceFactory: ReaderPagingSource.Factory,
//    val controller: ReaderController,
) : ViewModel() {
    val picturePagingFlow = Pager(
        PagingConfig(pageSize = 20, prefetchDistance = 3),
    ) {
        readerPagingSourceFactory("", 0) {
        }
    }
        .flow
        .cachedIn(viewModelScope)

}
