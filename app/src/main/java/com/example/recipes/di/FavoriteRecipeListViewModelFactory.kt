package com.example.recipes.di

import com.example.recipes.data.RecipesRepository
import com.example.recipes.ui.recipe.favorite.FavoriteRecipeListViewModel

class FavoriteRecipeListViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<FavoriteRecipeListViewModel>{

    override fun create(): FavoriteRecipeListViewModel {
        return FavoriteRecipeListViewModel(recipesRepository)
    }
}