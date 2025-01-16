package com.ferdsapp.moviefinder.core.data.model.network.search

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @field:SerializedName("page")
    var page: Int = 0,

    @field:SerializedName("results")
    var results: ArrayList<ListSearchResponse>,

    @field:SerializedName("total_pages")
    var total_pages: Int = 0,

    @field:SerializedName("total_results")
    var total_results: Int = 0,
)