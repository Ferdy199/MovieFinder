package com.ferdsapp.moviefinder.core.repository

import com.ferdsapp.moviefinder.core.data.source.RemoteDataSource

class MovieRepository private constructor(
    private val remoteDataSource: RemoteDataSource
) {
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
}