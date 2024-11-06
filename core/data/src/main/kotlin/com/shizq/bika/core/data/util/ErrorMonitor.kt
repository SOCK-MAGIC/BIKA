package com.shizq.bika.core.data.util

import kotlinx.coroutines.flow.Flow

/**
 * Interface for handling error messages.
 */
interface ErrorMonitor {
    val errorMessage: Flow<ErrorMessage?>
    val isOffline: Flow<Boolean>
    var offlineMessage: String?
    fun addShortErrorMessage(
        error: String,
        label: String? = null,
        successAction: (() -> Unit)? = null,
        failureAction: (() -> Unit)? = null,
    ): String?

    fun addLongErrorMessage(
        error: String,
        label: String? = null,
        successAction: (() -> Unit)? = null,
        failureAction: (() -> Unit)? = null,
    ): String?

    fun addIndefiniteErrorMessage(
        error: String,
        label: String? = null,
        successAction: (() -> Unit)? = null,
        failureAction: (() -> Unit)? = null,
    ): String?

    fun clearErrorMessage(id: String)
}
