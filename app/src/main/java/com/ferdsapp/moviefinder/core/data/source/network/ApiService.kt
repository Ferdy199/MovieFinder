package com.ferdsapp.moviefinder.core.data.source.network

import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.movie.ListMoviePlaying
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.tvShow.ListTvShowPlaying
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.QueryMap

interface ApiService {

    @GET("3/movie/now_playing")
    suspend fun getMoviePlayingList(
        @Header("Authorization") authToken: String,
        @QueryMap query: Map<String, String>
    ) : ListMoviePlaying


    @GET("3/tv/airing_today")
    suspend fun getTvShowPlayingList(
        @Header("Authorization") authToken: String,
        @QueryMap query: Map<String, String>
    ): ListTvShowPlaying
}