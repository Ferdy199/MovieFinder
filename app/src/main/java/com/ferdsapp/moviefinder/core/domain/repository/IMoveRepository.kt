package com.ferdsapp.moviefinder.core.domain.repository

import com.ferdsapp.moviefinder.core.data.model.entity.movie.MovieEntity
import com.ferdsapp.moviefinder.core.data.model.entity.tvShow.TvShowEntity
import com.ferdsapp.moviefinder.core.data.model.network.login.GetTokenLogin
import com.ferdsapp.moviefinder.core.data.model.network.login.LoginResponse
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.movie.ItemMovePlaying
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.tvShow.ItemTvShowPlaying
import com.ferdsapp.moviefinder.core.data.utils.ApiResponse
import kotlinx.coroutines.flow.Flow

interface IMoveRepository {
    fun getMoviePlaying(): Flow<ApiResponse<ArrayList<MovieEntity>>>
    fun getTvShowPlaying(): Flow<ApiResponse<ArrayList<TvShowEntity>>>
    fun getTokenLogin(): Flow<ApiResponse<GetTokenLogin>>
    fun isSessionValid(session: String): Flow<Boolean>
    fun getRequestTokenValidate(): Flow<ApiResponse<String>>
    fun saveTokenValidate(token: String)
    fun saveRequestToken(token: String)
    fun getRequestToken(): Flow<String>
    fun loginProcess(username: String, password: String): Flow<ApiResponse<LoginResponse>>
}