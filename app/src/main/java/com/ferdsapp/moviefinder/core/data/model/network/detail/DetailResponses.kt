package com.ferdsapp.moviefinder.core.data.model.network.detail

import com.ferdsapp.moviefinder.core.data.model.network.utils.ItemGenre
import com.google.gson.annotations.SerializedName

data class DetailResponses(
    @field:SerializedName("adult")
    var adult: String? = "",

    @field:SerializedName("backdrop_path")
    var backdrop_path: String? = "",

    @field:SerializedName("belongs_to_collection")
    var belongs_to_collection: String? = "",

    @field:SerializedName("budget")
    var budget: Int? = 0,

    @field:SerializedName("genres")
    var genres: ArrayList<ItemGenre>? = null,

    @field:SerializedName("homepage")
    var homepage: String? = "",

    @field:SerializedName("id")
    var id: Int? = 0,

    @field:SerializedName("original_language")
    var original_language: String? = "",

    @field:SerializedName("original_title")
    var original_title: String? = "",

    @field:SerializedName("overview")
    var overview: String? = "",

    @field:SerializedName("popularity")
    var popularity: Number? = 0.0,

    @field:SerializedName("poster_path")
    var poster_path: String? = "",

    @field:SerializedName("release_date")
    var release_date: String? = "",

    @field:SerializedName("status")
    var status: String? = "",

    @field:SerializedName("tagline")
    var tagline: String? = "",

    @field:SerializedName("title")
    var title: String? = "",

    @field:SerializedName("video")
    var video: Boolean? = false,

    @field:SerializedName("vote_average")
    var vote_average: Number? = 0,

    @field:SerializedName("vote_count")
    var vote_count: Number? = 0,

    )