package com.ferdsapp.moviefinder.viewModel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ferdsapp.moviefinder.core.domain.usecase.MovieUseCase

class MainViewModel(movieUseCase: MovieUseCase): ViewModel() {
    val movie = movieUseCase.getNowMoviePlaying().asLiveData()
}