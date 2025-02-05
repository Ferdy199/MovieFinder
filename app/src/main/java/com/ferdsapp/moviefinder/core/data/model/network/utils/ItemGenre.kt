package com.ferdsapp.moviefinder.core.data.model.network.utils

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemGenre(
    @field:SerializedName("id")
    var id: Int = 0,

    @field:SerializedName("name")
    var name: String = ""
): Parcelable