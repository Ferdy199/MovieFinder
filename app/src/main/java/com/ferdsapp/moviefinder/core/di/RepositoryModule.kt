package com.ferdsapp.moviefinder.core.di

import android.content.Context
import android.content.SharedPreferences
import com.ferdsapp.moviefinder.core.data.repository.MovieRepository
import com.ferdsapp.moviefinder.core.domain.repository.IMoveRepository
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, RepositoryUtilModule::class])
abstract class RepositoryModule {
    @Binds
    abstract fun provideRepository(movieRepository: MovieRepository) : IMoveRepository
}