package com.ferdsapp.moviefinder.core.domain.usecase

import com.ferdsapp.moviefinder.core.data.model.entity.movie.MovieEntity
import com.ferdsapp.moviefinder.core.data.model.entity.tvShow.TvShowEntity
import com.ferdsapp.moviefinder.core.data.model.network.login.GetTokenLogin
import com.ferdsapp.moviefinder.core.data.model.network.login.LoginResponse
import com.ferdsapp.moviefinder.core.data.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MovieUseCase {
    fun getNowMoviePlaying(): Flow<Resource<ArrayList<MovieEntity>>>
    fun getTvShowPlaying(): Flow<Resource<ArrayList<TvShowEntity>>>
    fun getLoginToken(): Flow<Resource<GetTokenLogin>>
    fun getSessionInvalid(session: String): Flow<Boolean>
    fun saveTokenValidate(token: String)
    fun getTokenValidate(): Flow<Resource<String>>
    fun getRequestToken(): Flow<String>
    fun loginProcess(username: String, password: String): Flow<Resource<LoginResponse>>
}