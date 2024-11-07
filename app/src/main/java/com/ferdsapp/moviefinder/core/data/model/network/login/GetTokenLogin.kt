package com.ferdsapp.moviefinder.core.data.model.network.login

data class GetTokenLogin(
    val success: Boolean,
    val expires_at: String,
    val request_token: String
)