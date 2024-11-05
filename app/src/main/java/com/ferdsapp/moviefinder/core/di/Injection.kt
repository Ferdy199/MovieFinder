package com.ferdsapp.moviefinder.core.di

import android.content.Context
import com.ferdsapp.moviefinder.core.data.source.RemoteDataSource
import com.ferdsapp.moviefinder.core.data.source.network.ApiConfig
import com.ferdsapp.moviefinder.core.domain.repository.MovieRepository
import com.ferdsapp.moviefinder.core.domain.usecase.MovieInteractor
import com.ferdsapp.moviefinder.core.domain.usecase.MovieUseCase

object Injection {
    fun provideRepository(context: Context) : MovieRepository {
        val remoteDataSource = RemoteDataSource.getInstance(ApiConfig.provideApiService())

        return MovieRepository.getInstance(remoteDataSource)
    }

    fun provideMovieUseCase(context: Context) : MovieUseCase{
        val repository = provideRepository(context)
        return MovieInteractor(repository)
    }
}