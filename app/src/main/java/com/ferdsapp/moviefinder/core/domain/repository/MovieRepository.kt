package com.ferdsapp.moviefinder.core.domain.repository

import com.ferdsapp.moviefinder.core.data.model.nowPlaying.movie.ItemMovePlaying
import com.ferdsapp.moviefinder.core.data.source.RemoteDataSource
import com.ferdsapp.moviefinder.core.data.utils.ApiResponse
import kotlinx.coroutines.flow.Flow

class MovieRepository private constructor(
    private val remoteDataSource: RemoteDataSource
) : IMoveRepository {
    //init manual injection
    companion object{
        @Volatile
        private var instance: MovieRepository? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource
        ) : MovieRepository {
            return instance ?: synchronized(this){
                instance ?: MovieRepository(remoteDataSource)
            }
        }
    }

    //function here
    override fun getMoviePlaying(): Flow<ApiResponse<ArrayList<ItemMovePlaying>>> {
        TODO("Not yet implemented")
    }


}