package com.ferdsapp.moviefinder.core.data.model.entity.detail

import android.os.Parcelable
import com.ferdsapp.moviefinder.core.data.model.network.utils.ItemGenre
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailEntity(

    var adult: String? = "",

    var backdrop_path: String? = "",

    var belongs_to_collection: String? = "",

    var budget: Int? = 0,

    var genres: ArrayList<ItemGenre>? = null,

    var homepage: String? = "",

    var id: Int? = 0,

    var original_language: String? = "",

    var original_title: String? = "",

    var overview: String? = "",

    var popularity: Number? = 0.0,

    var poster_path: String? = "",

    var release_date: String? = "",

    var status: String? = "",

    var tagline: String? = "",

    var title: String? = "",

    var video: Boolean? = false,

    var vote_average: Number? = 0,

    var vote_count: Number? = 0,
): Parcelable