package com.ferdsapp.moviefinder.core.domain.repository

import com.ferdsapp.moviefinder.core.data.model.network.login.GetTokenLogin
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.movie.ItemMovePlaying
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.tvShow.ItemTvShowPlaying
import com.ferdsapp.moviefinder.core.data.utils.ApiResponse
import kotlinx.coroutines.flow.Flow

interface IMoveRepository {
    fun getMoviePlaying(): Flow<ApiResponse<ArrayList<ItemMovePlaying>>>
    fun getTvShowPlaying(): Flow<ApiResponse<ArrayList<ItemTvShowPlaying>>>
    fun getTokenLogin(): Flow<ApiResponse<GetTokenLogin>>
    fun isSessionValid(session: String): Flow<Boolean>
    fun getRequestTokenValidate(): Flow<String>
    fun saveTokenValidate(token: String)
    fun saveRequestToken(token: String)
    fun getRequestToken(): Flow<String>
}