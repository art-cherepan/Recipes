package com.example.recipes.ui.recipe

import androidx.lifecycle.ViewModel
import com.example.recipes.model.Ingredient

data class RecipeUiState(
    val title: String? = null,
    val ingredients: List<Ingredient> = listOf(),
    val method: List<String> = listOf(),
    val imageUrl: String? = null,
)

class RecipeViewModel : ViewModel() {
}