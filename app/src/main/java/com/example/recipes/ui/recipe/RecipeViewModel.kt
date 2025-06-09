package com.example.recipes.ui.recipe

import androidx.lifecycle.ViewModel
import com.example.recipes.model.Ingredient
import com.example.recipes.model.Recipe

data class RecipeUiState(
    val recipe: Recipe? = null,
    val portionCount: Int = 0,
    val ingredients: List<Ingredient> = listOf(),
    val method: List<String> = listOf(),
    val imageUrl: String? = null,
)

class RecipeViewModel : ViewModel() {
}