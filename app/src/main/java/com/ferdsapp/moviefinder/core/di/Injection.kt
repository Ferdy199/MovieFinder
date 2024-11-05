package com.ferdsapp.moviefinder.core.di

import android.content.Context
import com.ferdsapp.moviefinder.core.data.source.RemoteDataSource
import com.ferdsapp.moviefinder.core.data.source.network.ApiConfig
import com.ferdsapp.moviefinder.core.repository.MovieRepository

object Injection {
    fun provideRepository(context: Context) : MovieRepository{
        val remoteDataSource = RemoteDataSource.getInstance(ApiConfig.provideApiService())

        return MovieRepository.getInstance(remoteDataSource)
    }
}