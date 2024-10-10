package com.shizq.bika.feature.interest

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SavedSearchQueryHandle : InstanceKeeper.Instance {
    private val _textFlow = MutableStateFlow("")
    fun getStateFlow() = _textFlow.asStateFlow()

    fun set(value: String) {
        _textFlow.value = value
    }
}
