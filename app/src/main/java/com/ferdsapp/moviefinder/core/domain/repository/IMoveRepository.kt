package com.ferdsapp.moviefinder.core.domain.repository

import com.ferdsapp.moviefinder.core.data.model.entity.movie.MovieEntity
import com.ferdsapp.moviefinder.core.data.model.entity.tvShow.TvShowEntity
import com.ferdsapp.moviefinder.core.data.model.network.login.GetTokenLogin
import com.ferdsapp.moviefinder.core.data.model.network.login.LoginResponse
import com.ferdsapp.moviefinder.core.data.utils.Resource
import kotlinx.coroutines.flow.Flow

interface IMoveRepository {
    fun getMoviePlaying(): Flow<Resource<ArrayList<MovieEntity>>>
    fun getTvShowPlaying(): Flow<Resource<ArrayList<TvShowEntity>>>
    fun getTokenLogin(): Flow<Resource<GetTokenLogin>>
    fun isSessionValid(session: String): Flow<Boolean>
    fun getRequestTokenValidate(): Flow<Resource<String>>
    fun saveTokenValidate(token: String)
    fun saveRequestToken(token: String)
    fun getRequestToken(): Flow<String>
    fun loginProcess(username: String, password: String): Flow<Resource<LoginResponse>>
}