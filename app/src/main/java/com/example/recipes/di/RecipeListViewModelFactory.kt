package com.example.recipes.di

import com.example.recipes.data.RecipesRepository
import com.example.recipes.ui.recipe.list.RecipeListViewModel

class RecipeListViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<RecipeListViewModel> {

    override fun create(): RecipeListViewModel {
        return RecipeListViewModel(recipesRepository)
    }
}