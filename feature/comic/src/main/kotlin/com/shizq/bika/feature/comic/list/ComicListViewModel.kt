package com.shizq.bika.feature.comic.list

import androidx.lifecycle.ViewModel
import com.shizq.bika.core.data.repository.CompositeComicListRepository
import com.shizq.bika.core.datastore.BikaPreferencesDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ComicListViewModel @Inject constructor(
//    comics: Comics,
    private val preferences: BikaPreferencesDataSource,
    compositeComicListRepository: CompositeComicListRepository,
) : ViewModel() {

    val comicFlow: Nothing = TODO()
//        combine(
//            preferences.userData,
//            SortState.flow,
//            PageMetaData.redirectedPageFlow,
//            ::Triple,
//        )
//            .flatMapLatest { (hide, sort, page) ->
//                compositeComicListRepository(
//                    comics,
//                    sort = sort,
//                    page = page,
//                    badHobbies = hide.badHobbies,
//                ) {
//                    PageMetaData.metadata = it
//                }
//            }
}
