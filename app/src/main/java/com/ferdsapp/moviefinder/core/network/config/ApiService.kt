package com.ferdsapp.moviefinder.core.network.config

import com.ferdsapp.moviefinder.core.network.model.nowPlaying.movie.ListMoviePlaying
import com.ferdsapp.moviefinder.core.network.model.nowPlaying.tvShow.ListTvShowPlaying
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface ApiService {
    @Headers("Authorization")
    @GET("3/movie/now_playing")
    fun getMoviePlayingList(@QueryMap query: Map<String, String>) : Call<ListMoviePlaying>

    @GET
    fun getTvShowPlayingList(@QueryMap query: Map<String, String>): Call<ListTvShowPlaying>
}