package com.example.recipes.di

import android.app.Application
import com.example.recipes.data.RecipesRepository
import com.example.recipes.ui.recipe.RecipeViewModel

class RecipeViewModelFactory(
    private val recipesRepository: RecipesRepository,
    private val application: Application,
) : Factory<RecipeViewModel> {

    override fun create(): RecipeViewModel {
        return RecipeViewModel(
            repository = recipesRepository,
            application = application,
        )
    }
}