package com.ferdsapp.moviefinder.core.data.model.entity.movie

data class MovieEntity(
    var adult: Boolean = false,

    var backdrop_path: String? = "",

    var genre_ids: ArrayList<Int>,

    var id: String,

    var original_language: String,

    var original_title: String,

    var overview: String,

    var popularity: Float,

    var poster_path: String? = "",

    var release_date: String,

    var title : String,

    var vote_average: Float,
)