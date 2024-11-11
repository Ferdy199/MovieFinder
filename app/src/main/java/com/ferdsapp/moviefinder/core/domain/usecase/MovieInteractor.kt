package com.ferdsapp.moviefinder.core.domain.usecase

import com.ferdsapp.moviefinder.core.domain.repository.IMoveRepository
import kotlinx.coroutines.flow.Flow

class MovieInteractor(private val movieRepository: IMoveRepository) : MovieUseCase {
    override fun getNowMoviePlaying() =  movieRepository.getMoviePlaying()
    override fun getTvShowPlaying() = movieRepository.getTvShowPlaying()
    override fun getLoginToken() = movieRepository.getTokenLogin()
    override fun getSessionInvalid(session: String) = movieRepository.isSessionValid(session)
    override fun saveTokenValidate(token: String) = movieRepository.saveTokenValidate(token)
    override fun getTokenValidate() = movieRepository.getRequestTokenValidate()
    override fun getRequestToken() = movieRepository.getRequestToken()
}