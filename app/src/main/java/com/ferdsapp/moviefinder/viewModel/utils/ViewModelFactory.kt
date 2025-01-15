package com.ferdsapp.moviefinder.viewModel.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ferdsapp.moviefinder.core.di.AppScope
import com.ferdsapp.moviefinder.core.di.Injection
import com.ferdsapp.moviefinder.core.domain.usecase.MovieUseCase
import com.ferdsapp.moviefinder.viewModel.login.LoginViewModel
import com.ferdsapp.moviefinder.viewModel.main.MainViewModel
import com.ferdsapp.moviefinder.viewModel.splash.SplashViewModel
import javax.inject.Inject

@AppScope
class ViewModelFactory @Inject constructor(private val movieUseCase: MovieUseCase): ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(movieUseCase) as T
            }
           modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
               LoginViewModel(movieUseCase) as T
           }
           modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
               SplashViewModel(movieUseCase) as T
           }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }

//    companion object {
//        @Volatile
//        private var instance: ViewModelFactory? = null
//
//        fun getInstance(context: Context): ViewModelFactory {
//            return instance ?: synchronized(this) {
//                instance ?: ViewModelFactory(Injection.provideMovieUseCase(context))
//            }
//        }
//    }
}