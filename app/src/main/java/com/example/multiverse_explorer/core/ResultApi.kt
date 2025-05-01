package com.example.multiverse_explorer.core

sealed class ResultApi<out T> {
    data class Success<T>(val data: T) : ResultApi<T>()
    data class Error(val message: String) : ResultApi<Nothing>()
}