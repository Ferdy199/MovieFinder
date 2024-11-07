package com.ferdsapp.moviefinder.core.domain.usecase

import com.ferdsapp.moviefinder.core.domain.repository.IMoveRepository

class MovieInteractor(private val movieRepository: IMoveRepository) : MovieUseCase {
    override fun getNowMoviePlaying() =  movieRepository.getMoviePlaying()
    override fun getTvShowPlaying() = movieRepository.getTvShowPlaying()
    override fun getLoginToken() = movieRepository.getTokenLogin()
}