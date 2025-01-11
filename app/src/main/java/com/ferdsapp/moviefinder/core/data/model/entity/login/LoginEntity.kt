package com.ferdsapp.moviefinder.core.data.model.entity.login

data class LoginEntity(
    val success: Boolean = false,
    val expires_at: String = "",
    val request_token: String = "",
    val status_code: Int = 0,
    val status_message: String = ""
)