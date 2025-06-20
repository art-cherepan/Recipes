package com.example.recipes.ui.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipes.model.Ingredient
import com.example.recipes.model.Recipe

data class RecipeUiState(
    val recipe: Recipe? = null,
    val portionCount: Int = 0,
    val ingredients: List<Ingredient> = listOf(),
    val method: List<String> = listOf(),
    val imageUrl: String? = null,
    val isFavorite: Boolean = false,
)

class RecipeViewModel : ViewModel() {
    private val mutableRecipeUiState = MutableLiveData<RecipeUiState>()
    val recipeUiState: LiveData<RecipeUiState> = mutableRecipeUiState

    init {
        mutableRecipeUiState.value = RecipeUiState()
        Log.i("!!!", "Init UI state. isFavorite: ${mutableRecipeUiState.value?.isFavorite}")
    }

    fun toggleFavorite() {
        val current = mutableRecipeUiState.value ?: RecipeUiState()
        val toggleFavorite = !current.isFavorite
        mutableRecipeUiState.value = current.copy(isFavorite = toggleFavorite)
    }
}