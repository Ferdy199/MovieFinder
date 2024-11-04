package com.ferdsapp.moviefinder.core.network.config

import com.ferdsapp.moviefinder.core.network.model.nowPlaying.movie.ListMoviePlaying
import com.ferdsapp.moviefinder.core.network.model.nowPlaying.tvShow.ListTvShowPlaying
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface ApiService {

    @GET("3/movie/now_playing")
    fun getMoviePlayingList(
        @Header("Authorization") authToken: String,
        @QueryMap query: Map<String, String>
    ) : Call<ListMoviePlaying>


    @GET("3/tv/airing_today")
    fun getTvShowPlayingList(
        @Header("Authorization") authToken: String,
        @QueryMap query: Map<String, String>
    ): Call<ListTvShowPlaying>
}