package com.ferdsapp.moviefinder.viewModel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ferdsapp.moviefinder.core.domain.usecase.MovieUseCase

class LoginViewModel(movieUseCase: MovieUseCase) : ViewModel() {
    val getTokenLogin = movieUseCase.getLoginToken().asLiveData()
}