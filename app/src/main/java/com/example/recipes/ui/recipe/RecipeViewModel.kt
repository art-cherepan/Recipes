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
import com.example.recipes.model.Recipe

data class RecipeUiState(
    val recipe: Recipe? = null,
    val portionCount: Int = 1,
    val isFavorite: Boolean = false,
    val recipeImage: Drawable? = null,
)

class RecipeViewModel(private val application: Application) : AndroidViewModel(application) {
    private val backendSingleton = BackendSingleton()
    private val _recipeState = MutableLiveData(RecipeUiState())
    val recipeState: LiveData<RecipeUiState> = _recipeState

    fun loadRecipe(id: Int?) {
        val recipe = backendSingleton.getRecipeById(id)
        val currentState = _recipeState.value ?: RecipeUiState()

        val favoriteRecipeIds = getFavoriteRecipeList()
        val isFavorite = id.toString() in favoriteRecipeIds

        val drawable = try {
            Drawable.createFromStream(
                application.assets?.open(recipe.imageUrl), null
            )
        } catch (e: Exception) {
            Log.e("ImageLoadError", "Image not found: ${recipe.imageUrl}", e)
            null
        }

        _recipeState.value = currentState.copy(
            recipe = recipe,
            isFavorite = isFavorite,
            portionCount = currentState.portionCount,
            recipeImage = drawable,
        )
    }

    fun onFavoriteRecipeListClicked(): Boolean? {
        val current = _recipeState.value ?: RecipeUiState()
        _recipeState.value = current.copy(isFavorite = !current.isFavorite)

        val favoriteRecipeIds: MutableSet<String> = getFavoriteRecipeList().toMutableSet()

        if (_recipeState.value?.isFavorite == true) {
            favoriteRecipeIds.add(_recipeState.value?.recipe?.id.toString())
            val immutableSet: Set<String> = favoriteRecipeIds

            saveFavoriteRecipeList(immutableSet)
        } else {
            favoriteRecipeIds.removeIf { it == _recipeState.value?.recipe?.id.toString() }
            val immutableSet: Set<String> = favoriteRecipeIds

            saveFavoriteRecipeList(immutableSet)
        }

        return _recipeState.value?.isFavorite
    }

    fun updatePortionCount(portionCount: Int) {
        val current = _recipeState.value ?: RecipeUiState()

        _recipeState.value = current.copy(
            portionCount = portionCount
        )
    }

    fun getFavoriteRecipeList(): Set<String> {
        val sharedPreferences = application.getSharedPreferences(
            Constants.FAVORITE_RECIPES_PREFERENCES,
            Context.MODE_PRIVATE,
        )

        return sharedPreferences?.getStringSet(
            Constants.FAVORITE_RECIPES_KEY,
            emptySet()
        )?.toSet() ?: emptySet()
    }

    private fun saveFavoriteRecipeList(favoriteRecipeIds: Set<String>) {
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