package com.ferdsapp.moviefinder.viewModel.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ferdsapp.moviefinder.core.di.Injection
import com.ferdsapp.moviefinder.core.domain.usecase.MovieUseCase
import com.ferdsapp.moviefinder.viewModel.main.MainViewModel

class ViewModelFactory private constructor(private val movieUseCase: MovieUseCase): ViewModelProvider.NewInstanceFactory(){
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideMovieUseCase(context))
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(movieUseCase) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}