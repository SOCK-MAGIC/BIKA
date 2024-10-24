package com.shizq.bika.feature.comic.list

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.data.repository.CompositeComicListRepository
import com.shizq.bika.core.datastore.BikaInterestsDataSource
import com.shizq.bika.core.network.model.Comics
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
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

    override val categoryVisibilityUiState = userInterests.userData.map { it.categoriesVisibility }
        .stateIn(
            componentScope,
            SharingStarted.WhileSubscribed(5000),
            emptyMap(),
        )
    override val comicFlow =
        compositeComicListRepository.getPagingFlowByCategories(comics)

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
