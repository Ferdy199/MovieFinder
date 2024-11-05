package com.ferdsapp.moviefinder.core.data.source

import com.ferdsapp.moviefinder.core.data.utils.ApiResponse
import com.ferdsapp.moviefinder.core.data.source.network.ApiService
import com.ferdsapp.moviefinder.core.data.model.nowPlaying.movie.ListMoviePlaying
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoteDataSource private constructor(private val apiService: ApiService) {

    //init manual injection
    companion object{
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(service: ApiService): RemoteDataSource {
            return instance ?: synchronized(this){
                instance ?: RemoteDataSource(service)
            }
        }
    }

    //function here

    suspend fun getMovieNowPlaying(): Flow<ApiResponse<List<ListMoviePlaying>>>{
        return flow {

        }
    }
}