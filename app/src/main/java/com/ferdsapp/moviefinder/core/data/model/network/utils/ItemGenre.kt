package com.ferdsapp.moviefinder.core.data.model.network.utils

import com.google.gson.annotations.SerializedName

data class ItemGenre(
    @field:SerializedName("id")
    var id: Int = 0,

    @field:SerializedName("name")
    var name: String = ""
)