package com.ferdsapp.moviefinder.core.data.source.network

import com.ferdsapp.moviefinder.core.data.model.network.detail.DetailResponses
import com.ferdsapp.moviefinder.core.data.model.network.login.GetTokenLogin
import com.ferdsapp.moviefinder.core.data.model.network.login.LoginResponse
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.movie.ListMoviePlaying
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.tvShow.ListTvShowPlaying
import com.ferdsapp.moviefinder.core.data.model.network.search.SearchResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiService {

    @GET("3/movie/now_playing")
    suspend fun getMoviePlayingList(
        @Header("Authorization") authToken: String,
        @QueryMap query: Map<String, String>
    ) : ListMoviePlaying


    @GET("3/tv/popular")
    suspend fun getTvShowPlayingList(
        @Header("Authorization") authToken: String,
        @QueryMap query: Map<String, String>
    ): ListTvShowPlaying

    @GET("3/authentication/token/new")
    suspend fun getLoginToken(@Header("Authorization") authToken: String) : GetTokenLogin

    @FormUrlEncoded
    @POST("3/authentication/token/validate_with_login")
    suspend fun loginProcess(
        @Header("Authorization") authToken: String,
        @Field("request_token") requestToken: String,
        @Field("username") username: String,
        @Field("password") password: String,
    ): LoginResponse

    @GET("/3/search/multi")
    suspend fun getSearch(
        @Header("Authorization") authToken: String,
        @QueryMap query: Map<String, String>
    ): SearchResponse

    @GET("https://api.themoviedb.org/3/{type}/{id}")
    suspend fun getDetails(
        @Header("Authorization") authToken: String,
        @Path("type") type: String,
        @Path("id") id: String
    ): DetailResponses
}