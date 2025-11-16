package com.example.recipes

import android.app.Application
import com.example.recipes.di.AppContainer

class RecipesApplication: Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        appContainer = AppContainer(this)
    }
}