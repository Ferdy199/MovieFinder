package com.ferdsapp.moviefinder.core.domain.usecase

import com.ferdsapp.moviefinder.core.data.model.nowPlaying.movie.ItemMovePlaying
import com.ferdsapp.moviefinder.core.data.utils.ApiResponse
import kotlinx.coroutines.flow.Flow

interface MovieUseCase {
    fun getNowMoviePlaying(): Flow<ApiResponse<ArrayList<ItemMovePlaying>>>
}