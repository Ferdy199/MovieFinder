package com.ferdsapp.moviefinder.core.domain.usecase

import com.ferdsapp.moviefinder.core.data.model.nowPlaying.movie.ItemMovePlaying
import com.ferdsapp.moviefinder.core.data.utils.ApiResponse
import com.ferdsapp.moviefinder.core.domain.repository.IMoveRepository
import kotlinx.coroutines.flow.Flow

class MovieInteractor(private val movieRepository: IMoveRepository) : MovieUseCase {
    override fun getMoviePlaying(): Flow<ApiResponse<ArrayList<ItemMovePlaying>>> {
        return movieRepository.getMoviePlaying()
    }
}