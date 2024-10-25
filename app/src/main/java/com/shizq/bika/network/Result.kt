package com.shizq.bika.network

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(
        val code: Int = 0,
        val error: String = "",
        val message: String = "",
        val exception: Throwable? = null,
    ) : Result<Nothing>

    data object Loading : Result<Nothing>
}

