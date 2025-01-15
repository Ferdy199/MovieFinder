package com.ferdsapp.moviefinder.core.di

import com.ferdsapp.moviefinder.ui.login.LoginActivity
import com.ferdsapp.moviefinder.ui.main.MainActivity
import com.ferdsapp.moviefinder.ui.splash.SplashActivity
import dagger.Component

@AppScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [AppModule::class]
)
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): AppComponent
    }

    fun inject(activity: MainActivity)
    fun inject(loginActivity: LoginActivity)
    fun inject(splashActivity: SplashActivity)
}