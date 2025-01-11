package com.ferdsapp.moviefinder.viewModel.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ferdsapp.moviefinder.core.domain.usecase.MovieUseCase

class SplashViewModel(private val movieUseCase: MovieUseCase) : ViewModel() {
    fun getSessionInvalid(session: String) = movieUseCase.getSessionInvalid(session).asLiveData()
    fun getTokenLogin() = movieUseCase.getLoginToken().asLiveData()
}