package com.ferdsapp.moviefinder.viewModel.main

import androidx.lifecycle.ViewModel
import com.ferdsapp.moviefinder.core.domain.usecase.MovieUseCase

class MainViewModel(movieUseCase: MovieUseCase): ViewModel() {
    val movie = movieUseCase.getMoviePlaying()
}