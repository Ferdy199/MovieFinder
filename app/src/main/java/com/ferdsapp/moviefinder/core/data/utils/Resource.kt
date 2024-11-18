package com.ferdsapp.moviefinder.core.data.utils

sealed class Resource<out T> {
    data class Success<out T>(val data: T): Resource<T>()
    data class Error<T>(val message: String, val data: T? = null) : Resource<T>()
    data object Empty : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}