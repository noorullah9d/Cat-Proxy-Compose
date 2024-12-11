package com.galixo.proxy.compose.utils

/*
sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}*/
sealed class Result<out T> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error<out T>(val message: String) : Result<T>()
    data class Loading<out T>(val message: String) : Result<T>()


    companion object {

        fun <T> error(message: String): Result<T> = Error(message)
        fun <T> loading(message: String): Result<T> = Loading(message)
        fun <T> success(value: T): Result<T> = Success(value)
    }
}