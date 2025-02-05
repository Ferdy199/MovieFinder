package com.ferdsapp.moviefinder.core.di

import com.ferdsapp.moviefinder.ui.detail.DetailFragment
import com.ferdsapp.moviefinder.ui.home.HomeFragment
import dagger.Module
import dagger.Provides

@Module
class FragmentModule {
    @Provides
    fun provideHomeFragment(): HomeFragment {
        return HomeFragment()
    }

    @Provides
    fun provideDetailFragment() : DetailFragment {
        return DetailFragment()
    }
}