package com.ferdsapp.moviefinder.application

import android.app.Application
import com.ferdsapp.moviefinder.core.di.AppComponent
import com.ferdsapp.moviefinder.core.di.CoreComponent
import com.ferdsapp.moviefinder.core.di.DaggerAppComponent
import com.ferdsapp.moviefinder.core.di.DaggerCoreComponent

open class MyApplication : Application(){
    private val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.factory().create(applicationContext)
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(coreComponent)
    }
}