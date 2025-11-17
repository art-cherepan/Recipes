package com.example.recipes.di

import com.example.recipes.data.RecipesRepository
import com.example.recipes.ui.category.CategoryListViewModel

class CategoryListViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<CategoryListViewModel> {

    override fun create(): CategoryListViewModel {
        return CategoryListViewModel(recipesRepository)
    }
}