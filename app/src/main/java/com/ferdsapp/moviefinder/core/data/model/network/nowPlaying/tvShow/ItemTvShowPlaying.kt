package com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.tvShow

import com.google.gson.annotations.SerializedName

data class ItemTvShowPlaying(
    @field:SerializedName("adult")
    val adult: Boolean = false,

    @field:SerializedName("backdrop_path")
    val backdrop_path: String,

    @field:SerializedName("genre_ids")
    val genre_ids: ArrayList<Int>,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("origin_country")
    val origin_country: ArrayList<String>,

    @field:SerializedName("original_language")
    val original_language: String,

    @field:SerializedName("original_name")
    val original_name: String,

    @field:SerializedName("overview")
    val overview: String,

    @field:SerializedName("popularity")
    val popularity: Float,

    @field:SerializedName("poster_path")
    val poster_path: String,

    @field:SerializedName("first_air_date")
    val first_air_date: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("vote_average")
    val vote_average: Int,

    @field:SerializedName("vote_count")
    val vote_count: Int
)