package com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.tvShow

import com.google.gson.annotations.SerializedName

data class ListTvShowPlaying(
    @field:SerializedName("page")
    val page: Int,

    @field:SerializedName("results")
    val results: ArrayList<ItemTvShowPlaying>,

    @field:SerializedName("total_pages")
    val total_pages: Int,

    @field:SerializedName("total_results")
    val total_results: Int
)