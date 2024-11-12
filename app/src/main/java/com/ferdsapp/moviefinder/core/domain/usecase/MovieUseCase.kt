package com.ferdsapp.moviefinder.core.domain.usecase

import com.ferdsapp.moviefinder.core.data.model.network.login.GetTokenLogin
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.movie.ItemMovePlaying
import com.ferdsapp.moviefinder.core.data.model.network.nowPlaying.tvShow.ItemTvShowPlaying
import com.ferdsapp.moviefinder.core.data.utils.ApiResponse
import kotlinx.coroutines.flow.Flow

interface MovieUseCase {
    fun getNowMoviePlaying(): Flow<ApiResponse<ArrayList<ItemMovePlaying>>>
    fun getTvShowPlaying(): Flow<ApiResponse<ArrayList<ItemTvShowPlaying>>>
    fun getLoginToken(): Flow<ApiResponse<GetTokenLogin>>
    fun getSessionInvalid(session: String): Flow<Boolean>
    fun saveTokenValidate(token: String)
    fun getTokenValidate(): Flow<ApiResponse<String>>
    fun getRequestToken(): Flow<String>
}