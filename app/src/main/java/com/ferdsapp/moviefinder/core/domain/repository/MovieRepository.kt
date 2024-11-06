package com.ferdsapp.moviefinder.core.domain.repository

import android.util.Log
import com.ferdsapp.moviefinder.core.data.model.nowPlaying.movie.ItemMovePlaying
import com.ferdsapp.moviefinder.core.data.source.RemoteDataSource
import com.ferdsapp.moviefinder.core.data.utils.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

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
        return flow {
            try {
                remoteDataSource
                    .getMovieNowPlaying()
                    .collect { apiResponse ->
                        when(apiResponse){
                            is ApiResponse.Success -> {
                                Log.d("MovieFinder Repository", "response Success")
                                emit(ApiResponse.Success(apiResponse.data))
                            }
                            is ApiResponse.Empty -> {
                                Log.d("MovieFinder Repository", "response Empty")
                                emit(ApiResponse.Empty)
                            }
                            is ApiResponse.Error -> {
                                Log.d("MovieFinder Repository", "response Error")
                                emit(ApiResponse.Error(apiResponse.errorMessage))
                            }
                        }
                    }
            }catch (e : Exception){
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}