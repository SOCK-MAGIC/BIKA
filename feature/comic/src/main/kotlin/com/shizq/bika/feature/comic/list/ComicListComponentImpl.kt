package com.shizq.bika.feature.comic.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.data.repository.CompositeComicListRepository
import com.shizq.bika.core.datastore.BikaInterestsDataSource
import com.shizq.bika.core.network.model.Comics
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ComicListComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted comics: Comics,
    private val userInterests: BikaInterestsDataSource,
    compositeComicListRepository: CompositeComicListRepository,
) : ComicListComponent,
    ComponentContext by componentContext {
    override val categoryVisibilityUiState = userInterests.userData.map { it.categoriesVisibility }
        .stateIn(
            componentScope,
            SharingStarted.WhileSubscribed(5000),
            emptyMap(),
        )

    override val comicFlow =
        combine(
            userInterests.userHideCategories,
            SortState.flow,
            PageMetaData.redirectedPageFlow,
            ::Triple,
        )
            .flatMapLatest { (hide, sort, page) ->
                compositeComicListRepository.getPagingFlowByCategories(
                    comics,
                    sort = sort,
                    page = page,
                    hide = hide,
                ) {
                    PageMetaData.pageMetadata = it
                }
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
        ): ComicListComponentImpl
    }
}
