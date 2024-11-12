package com.ferdsapp.moviefinder.viewModel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ferdsapp.moviefinder.core.domain.usecase.MovieUseCase

class LoginViewModel(private val movieUseCase: MovieUseCase) : ViewModel() {
    val getTokenLogin = movieUseCase.getLoginToken().asLiveData()
    val getTokenValidate = movieUseCase.getTokenValidate().asLiveData()
    val getRequestToken = movieUseCase.getRequestToken().asLiveData()
    fun getSessionInvalid(key: String) = movieUseCase.getSessionInvalid(key).asLiveData()
    fun saveTokenValidate(token: String) = movieUseCase.saveTokenValidate(token)
}