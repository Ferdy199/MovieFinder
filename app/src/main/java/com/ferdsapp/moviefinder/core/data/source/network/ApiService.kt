package com.ferdsapp.moviefinder.core.data.source.network

import com.ferdsapp.moviefinder.core.data.model.nowPlaying.movie.ListMoviePlaying
import com.ferdsapp.moviefinder.core.data.model.nowPlaying.tvShow.ListTvShowPlaying
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
    ): Call<ListTvShowPlaying>
}