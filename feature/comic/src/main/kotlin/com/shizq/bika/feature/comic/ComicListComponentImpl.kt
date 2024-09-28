package com.shizq.bika.feature.comic

import android.util.Log
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.Sort
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class ComicListComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted category: String,
    private val network: BikaNetworkDataSource,
) : ComicListComponent, ComponentContext by componentContext {
    init {
        componentScope.launch {
            val response = network.getComicList(
                sort = Sort.SORT_TIME_NEWEST,
                page = 1,
                category = category
            )
            Log.d("ComicListComponentImpl", category)
            Log.d("ComicListComponentImpl", "ComicList: $response")
        }
    }

    @AssistedFactory
    interface Factory : ComicListComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            category: String,
        ): ComicListComponentImpl
    }
}
