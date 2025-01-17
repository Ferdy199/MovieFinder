package com.ferdsapp.moviefinder.core.data.model.entity.search

import com.google.gson.annotations.SerializedName

data class ListSearchEntity(
    var adult: Boolean = false,

    var backdrop_path: String? = "",

    var id: Int = 0,

    var title : String,

    var original_language: String,

    var original_title: String,

    var overview: String,

    var poster_path: String? = "",

    var genre_ids: ArrayList<Int>,

    var popularity: Number,

    var release_date: String,

    var vote_average: Number,

    var vote_count: Int,
)