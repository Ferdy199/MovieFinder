package com.ferdsapp.moviefinder.core.data.model.entity.movie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieEntity(
    var adult: Boolean = false,

    var backdrop_path: String? = "",

    var genre_ids: ArrayList<Int>,

    var id: Int,

    var original_language: String,

    var original_title: String,

    var overview: String,

    var popularity: Float,

    var poster_path: String? = "",

    var media_type: String? = "",

    var release_date: String,

    var title : String,

    var vote_average: Float,
) : Parcelable