package com.shizq.bika.core.datastore

import androidx.datastore.core.DataStore
import com.shizq.bika.core.datastore.model.UserInterests
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@Deprecated("")
class BikaInterestsDataSource @Inject constructor(
    userInterests: DataStore<UserInterests>,
) {
    val userHideCategories =
        userInterests.data.map { interests -> interests.categoriesVisibility.filterValues { !it }.keys }
}
