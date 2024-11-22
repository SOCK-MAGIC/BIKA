package com.shizq.bika.feature.comic.list

import com.arkivanov.decompose.ComponentContext
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.data.repository.CompositeComicListRepository
import com.shizq.bika.core.datastore.BikaPreferencesDataSource
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
    private val preferences: BikaPreferencesDataSource,
    compositeComicListRepository: CompositeComicListRepository,
) : ComicListComponent,
    ComponentContext by componentContext {

    override val comicFlow =
        combine(
            preferences.userData,
            SortState.flow,
            PageMetaData.redirectedPageFlow,
            ::Triple,
        )
            .flatMapLatest { (hide, sort, page) ->
                compositeComicListRepository(
                    comics,
                    sort = sort,
                    page = page,
                    badHobbies = hide.badHobbies,
                ) {
                    PageMetaData.metadata = it
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
