package com.ferdsapp.moviefinder.core.di

import com.ferdsapp.moviefinder.core.domain.usecase.MovieInteractor
import com.ferdsapp.moviefinder.core.domain.usecase.MovieUseCase
import dagger.Binds
import dagger.Module

@Module
abstract class AppModule {
    @Binds
    abstract fun provideMovieUseCase(movieInteractor: MovieInteractor): MovieUseCase
}