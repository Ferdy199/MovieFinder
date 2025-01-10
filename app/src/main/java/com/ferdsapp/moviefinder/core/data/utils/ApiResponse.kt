package com.ferdsapp.moviefinder.core.data.utils

sealed class ApiResponse <out T> {
    data class Success<out T>(val data: T): ApiResponse<T>()
    data class Error<T>(val errorMessage: String, val data: T? = null): ApiResponse<T>()
    data object Empty : ApiResponse<Nothing>()
    data object Loading : ApiResponse<Nothing>()
}