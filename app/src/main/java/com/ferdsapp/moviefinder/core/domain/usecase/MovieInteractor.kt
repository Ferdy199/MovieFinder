package com.ferdsapp.moviefinder.core.domain.usecase

import com.ferdsapp.moviefinder.core.data.model.entity.detail.DetailEntity
import com.ferdsapp.moviefinder.core.data.model.entity.search.ListSearchEntity
import com.ferdsapp.moviefinder.core.data.utils.Resource
import com.ferdsapp.moviefinder.core.domain.repository.IMoveRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieInteractor @Inject constructor(private val movieRepository: IMoveRepository) : MovieUseCase {
    override fun getNowMoviePlaying() =  movieRepository.getMoviePlaying()
    override fun getTvShowPlaying() = movieRepository.getTvShowPlaying()
    override fun getRequestToken() = movieRepository.getRequestToken()
    override fun getSessionInvalid(session: String) = movieRepository.isSessionValid(session)
    override fun saveTokenValidate(token: String) = movieRepository.saveTokenValidate(token)
    override fun getTokenValidate() = movieRepository.getRequestTokenValidate()
    override fun getLoginToken() = movieRepository.getTokenLogin()
    override fun loginProcess(username: String, password: String) = movieRepository.loginProcess(username, password)
    override fun getSearch(search: String): Flow<Resource<ArrayList<ListSearchEntity>>> = movieRepository.getSearch(search)
    override fun getDetailMovie(type: String, id: Int): Flow<Resource<DetailEntity>> = movieRepository.getDetailMovie(type, id)
}