package com.ferdsapp.moviefinder.viewModel.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ferdsapp.moviefinder.core.domain.usecase.MovieUseCase

class SearchViewModel(private val movieUseCase: MovieUseCase): ViewModel() {
    fun getSearch(search: String) = movieUseCase.getSearch(search).asLiveData()
}