package com.shizq.bika.feature.interest

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.essenty.statekeeper.StateKeeperOwner
import com.shizq.bika.core.component.componentScope
import com.shizq.bika.core.network.BikaNetworkDataSource
import com.shizq.bika.core.network.model.NetworkCategories
import com.shizq.bika.core.result.Result
import com.shizq.bika.core.result.asResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class InterestComponentImpl @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    private val network: BikaNetworkDataSource,
) : InterestComponent, ComponentContext by componentContext {
    override val interestUiState = flow { emit(network.getCategories()) }
        .asResult()
        .map { result ->
            when (result) {
                is Result.Error -> InterestsUiState.Empty
                Result.Loading -> InterestsUiState.Loading
                is Result.Success -> InterestsUiState.Interests(result.data.mapToInterests())
            }
        }.stateIn(
            scope = componentScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = InterestsUiState.Loading,
        )
    private val searchQueryHandle = instanceKeeper.getOrCreate { SavedSearchQueryHandle() }

    override val searchQuery = searchQueryHandle.getStateFlow()
    override fun onSearchQueryChanged(query: String) {
        searchQueryHandle.set(query)
    }

    override fun onSearchTriggered(query: String) {
        componentScope.launch {
            network.advanced_search(query, 1)
        }
    }

    @AssistedFactory
    interface Factory : InterestComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ): InterestComponentImpl
    }
}

sealed interface InterestsUiState {
    data object Loading : InterestsUiState

    data class Interests(val interests: List<Interest>) : InterestsUiState

    data object Empty : InterestsUiState
}

data class Interest(
    val id: String,
    val isWeb: Boolean,
    val link: String,
    val title: String,
    val imageUrl: String,
)

private fun NetworkCategories.mapToInterests() = categories.map {
    if (!arrayOf("嗶咔小禮物", "嗶咔畫廊").contains(it.title)) {
        Interest(
            it.id,
            it.isWeb,
            it.link,
            it.title,
            it.thumb.imageUrl,
        )
    } else {
        Interest(
            it.id,
            it.isWeb,
            it.link,
            it.title,
            "https://s3.picacomic.com/static/${it.thumb.path}",
        )
    }
}

@Deprecated("decompose experimental since version 3.2.0-alpha02")
inline fun <T : Any> StateKeeper.saveable(
    serializer: KSerializer<T>,
    key: String? = null,
    crossinline init: () -> T,
): PropertyDelegateProvider<Any?, ReadWriteProperty<Any?, T>> =
    PropertyDelegateProvider { _, property ->
        val stateKey = key ?: "SAVEABLE_${property.name}"
        var saveable = consume(key = stateKey, strategy = serializer) ?: init()
        register(key = stateKey, strategy = serializer) { saveable }

        object : ReadWriteProperty<Any?, T> {
            override fun getValue(thisRef: Any?, property: KProperty<*>): T =
                saveable

            override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
                saveable = value
            }
        }
    }

inline fun <T : Any> StateKeeperOwner.saveable(
    serializer: KSerializer<T>,
    key: String? = null,
    crossinline init: () -> T,
): PropertyDelegateProvider<Any?, ReadWriteProperty<Any?, T>> =
    stateKeeper.saveable(
        serializer = serializer,
        key = key,
        init = init,
    )

private const val SEARCH_QUERY = "searchQuery"