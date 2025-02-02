package com.ferdsapp.moviefinder.core.di

import com.ferdsapp.moviefinder.ui.detail.DetailFragment
import com.ferdsapp.moviefinder.ui.home.HomeFragment
import com.ferdsapp.moviefinder.ui.login.LoginActivity
import com.ferdsapp.moviefinder.ui.main.MainActivity
import com.ferdsapp.moviefinder.ui.search.SearchFragment
import com.ferdsapp.moviefinder.ui.splash.SplashActivity
import dagger.Component

@AppScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [AppModule::class, FragmentModule::class]
)
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(coreComponent: CoreComponent): AppComponent
    }

    fun inject(activity: MainActivity)
    fun inject(fragment: HomeFragment)
    fun inject(loginActivity: LoginActivity)
    fun inject(splashActivity: SplashActivity)
    fun inject(fragmentSearch: SearchFragment)
    fun inject(detailFragment: DetailFragment)
}