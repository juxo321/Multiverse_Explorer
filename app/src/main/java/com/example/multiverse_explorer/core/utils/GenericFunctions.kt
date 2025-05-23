package com.example.multiverse_explorer.core.utils

import com.example.multiverse_explorer.core.ResultApi

object NetworkFunctions {

    suspend fun <T, R> safeServiceCall(
        serviceCall: suspend () -> ResultApi<T>,
        transform: (T) -> R
    ): ResultApi<R> {
        val result = serviceCall()
        return when (result) {
            is ResultApi.Success -> ResultApi.Success(transform(result.data))
            is ResultApi.Error -> ResultApi.Error(result.message)
        }
    }

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): ResultApi<T> {
        return runCatching {
            apiCall()
        }.fold(
            onSuccess = { ResultApi.Success(it) },
            onFailure = { ResultApi.Error("Exception: ${it.message}") }
        )
    }

}