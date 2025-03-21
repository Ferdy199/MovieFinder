package com.ferdsapp.moviefinder.core.data.model.entity.tvShow

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TvShowEntity(

    val adult: Boolean = false,

    val backdrop_path: String? = "",

    val genre_ids: ArrayList<Int>,

    val id: Int,

    val origin_country: ArrayList<String>,

    val original_language: String,

    val original_name: String,

    val overview: String,

    val popularity: Float,

    val poster_path: String? = "",

    var media_type: String? = "",

    val first_air_date: String,

    val name: String,

    val vote_average: Float,

    val vote_count: Int
) : Parcelable