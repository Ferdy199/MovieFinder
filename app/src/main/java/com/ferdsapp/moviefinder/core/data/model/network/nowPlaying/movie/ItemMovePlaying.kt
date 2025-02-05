package com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.movie

import com.google.gson.annotations.SerializedName

data class ItemMovePlaying(
    @field:SerializedName("adult")
    var adult: Boolean = false,

    @field:SerializedName("backdrop_path")
    var backdrop_path: String,

    @field:SerializedName("genre_ids")
    var genre_ids: ArrayList<Int>,

    @field:SerializedName("id")
    var id: Int,

    @field:SerializedName("original_language")
    var original_language: String,

    @field:SerializedName("original_title")
    var original_title: String,

    @field:SerializedName("overview")
    var overview: String,

    @field:SerializedName("popularity")
    var popularity: Float,

    @field:SerializedName("poster_path")
    var poster_path: String,

    @field:SerializedName("media_type")
    var media_type: String,

    @field:SerializedName("release_date")
    var release_date: String,

    @field:SerializedName("title")
    var title : String,

    @field:SerializedName("vote_average")
    var vote_average: Float,
)