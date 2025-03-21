package com.ferdsapp.moviefinder.core.di

import android.content.Context
import com.ferdsapp.moviefinder.core.domain.repository.IMoveRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [RepositoryModule::class]
)
interface CoreComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): CoreComponent
    }

    fun provideRepository(): IMoveRepository
}