package com.shizq.bika.feature.comic.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.data.repository.CompositeComicListRepository
import com.shizq.bika.core.datastore.BikaInterestsDataSource
import com.shizq.bika.core.network.model.Comics
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ComicListComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted comics: Comics,
    @Assisted("page") page: Int? = null,
    private val userInterests: BikaInterestsDataSource,
    compositeComicListRepository: CompositeComicListRepository,
) : ComicListComponent,
    ComponentContext by componentContext {
    override var currentPage by mutableIntStateOf(1)
    override var totalPage by mutableIntStateOf(1)
    private var skipPage = 0
    override val categoryVisibilityUiState = userInterests.userData.map { it.categoriesVisibility }
        .stateIn(
            componentScope,
            SharingStarted.WhileSubscribed(5000),
            emptyMap(),
        )

    @OptIn(FlowPreview::class)
    override val comicFlow = snapshotFlow { skipPage }
        .sample(1000)
        .flatMapLatest {
            compositeComicListRepository.getPagingFlowByCategories(comics) {
                currentPage = it.currentPage
                totalPage = it.totalPages
            }
        }

    fun setSkipPageNumber(index: Int) {
        skipPage = index
    }

    override fun updateCategoryState(name: String, state: Boolean) {
        componentScope.launch {
            userInterests.setCategoriesVisibility(name, state)
        }
    }

    @AssistedFactory
    interface Factory : ComicListComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            comics: Comics,
            @Assisted("page") page: Int?,
        ): ComicListComponentImpl
    }
}
