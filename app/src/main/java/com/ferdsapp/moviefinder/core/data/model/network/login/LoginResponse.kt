package com.ferdsapp.moviefinder.core.data.model.network.login

data class LoginResponse(
    val success: Boolean = false,
    val expires_at: String,
    val status_code: Int,
    val status_message: String
)