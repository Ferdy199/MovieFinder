package com.ferdsapp.moviefinder.core.data.model.network.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("success")
    val success: Boolean = false,

    @field:SerializedName("expires_at")
    val expires_at: String = "",

    @field:SerializedName("request_token")
    val request_token: String = "",

    @field:SerializedName("status_code")
    val status_code: Int = 0,

    @field:SerializedName("status_message")
    val status_message: String = ""
)