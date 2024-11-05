package com.ferdsapp.moviefinder.core.domain.repository

import com.ferdsapp.moviefinder.core.data.model.nowPlaying.movie.ItemMovePlaying
import com.ferdsapp.moviefinder.core.data.utils.ApiResponse
import kotlinx.coroutines.flow.Flow

interface IMoveRepository {
    fun getMoviePlaying(): Flow<ApiResponse<ArrayList<ItemMovePlaying>>>
}