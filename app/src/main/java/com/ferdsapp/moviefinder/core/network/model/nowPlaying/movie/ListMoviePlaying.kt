package com.ferdsapp.moviefinder.core.network.model.nowPlaying.movie

import com.google.gson.annotations.SerializedName

data class ListMoviePlaying(
    @field:SerializedName("page")
    var page: Int,

    @field:SerializedName("results")
    var results: ArrayList<ItemMovePlaying>,

    @field:SerializedName("total_pages")
    var total_pages: Int,

)