package com.example.recipes.ui.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recipes.Constants
import com.example.recipes.data.BackendSingleton
import com.example.recipes.model.Ingredient
import com.example.recipes.model.Recipe

data class RecipeUiState(
    val recipe: Recipe? = null,
    val portionCount: Int = 0,
    val ingredients: List<Ingredient> = listOf(),
    val method: List<String> = listOf(),
    val isFavorite: Boolean = false,
    val recipeImage: Drawable? = null,
)

class RecipeViewModel(private val application: Application) : AndroidViewModel(application) {
    private val backendSingleton = BackendSingleton()
    private val _recipeState = MutableLiveData(RecipeUiState())
    val recipeState: LiveData<RecipeUiState> = _recipeState

    fun loadRecipe(id: Int?) {
        val recipe = backendSingleton.getRecipeById(id)
        val current = _recipeState.value ?: RecipeUiState()

        val favoriteRecipeIds = getFavorites()
        val isFavorite = id.toString() in favoriteRecipeIds

        val drawable = try {
            Drawable.createFromStream(
                application.assets?.open(recipe.imageUrl), null
            )
        } catch (e: Exception) {
            Log.e("ImageLoadError", "Image not found: ${recipe.imageUrl}", e)
            null
        }

        _recipeState.value = current.copy(
            recipe = recipe,
            isFavorite = isFavorite,
            portionCount = current.portionCount,
            recipeImage = drawable,
        )
    }

    fun onFavoritesClicked(): Boolean? {
        val current = _recipeState.value ?: RecipeUiState()
        _recipeState.value = current.copy(isFavorite = !current.isFavorite)

        val favoriteRecipeIds: MutableSet<String> = getFavorites().toMutableSet()

        if (_recipeState.value?.isFavorite == true) {
            favoriteRecipeIds.add(_recipeState.value?.recipe?.id.toString())
            val immutableSet: Set<String> = favoriteRecipeIds

            saveFavorites(immutableSet)
        } else {
            favoriteRecipeIds.removeIf { it == _recipeState.value?.recipe?.id.toString() }
            val immutableSet: Set<String> = favoriteRecipeIds

            saveFavorites(immutableSet)
        }

        return _recipeState.value?.isFavorite
    }


    fun getFavorites(): Set<String> {
        val sharedPreferences = application.getSharedPreferences(
            Constants.FAVORITE_RECIPES_PREFERENCES,
            Context.MODE_PRIVATE,
        )

        return sharedPreferences?.getStringSet(
            Constants.FAVORITE_RECIPES_KEY,
            emptySet()
        )?.toSet() ?: emptySet()
    }

    private fun saveFavorites(favoriteRecipeIds: Set<String>) {
        val sharedPreferences = application.getSharedPreferences(
            Constants.FAVORITE_RECIPES_PREFERENCES,
            Context.MODE_PRIVATE,
        )
        sharedPreferences?.edit {
            putStringSet(
                Constants.FAVORITE_RECIPES_KEY,
                favoriteRecipeIds,
            )
            apply()
        }
    }
}