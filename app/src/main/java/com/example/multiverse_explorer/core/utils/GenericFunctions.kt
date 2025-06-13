package com.example.multiverse_explorer.core.utils

import com.example.multiverse_explorer.core.ResultApi

object NetworkFunctions {

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): ResultApi<T> {
        return runCatching {
            apiCall()
        }.fold(
            onSuccess = { ResultApi.Success(it) },
            onFailure = { ResultApi.Error("Exception: ${it.message}") }
        )
    }

    suspend fun <T> safeServiceCallCharacters(
        serviceCall: suspend () -> ResultApi<T>,
    ): ResultApi<T> {
        val result = serviceCall()
        return when (result) {
            is ResultApi.Success -> ResultApi.Success(result.data)
            is ResultApi.Error -> ResultApi.Error(result.message)
        }
    }

    suspend fun <T, R> safeServiceCallCharacterDetail(
        serviceCall: suspend () -> ResultApi<T>,
        transform: suspend (T) -> R
    ): ResultApi<R> {
        val result = serviceCall()
        return when(result){
            is ResultApi.Success -> ResultApi.Success(transform(result.data))
            is ResultApi.Error -> ResultApi.Error(result.message)
        }
    }

}