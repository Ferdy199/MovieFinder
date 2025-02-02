package com.ferdsapp.moviefinder.viewModel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ferdsapp.moviefinder.core.domain.usecase.MovieUseCase

class MainViewModel(private val movieUseCase: MovieUseCase): ViewModel() {
    val movie = movieUseCase.getNowMoviePlaying().asLiveData()
    val tvShow = movieUseCase.getTvShowPlaying().asLiveData()
    fun getDetailMovie(type: String, id: Int) = movieUseCase.getDetailMovie(type, id).asLiveData()
}