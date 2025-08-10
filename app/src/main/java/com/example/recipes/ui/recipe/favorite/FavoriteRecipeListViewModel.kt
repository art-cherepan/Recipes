package com.example.recipes.ui.recipe.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipes.data.BackendSingleton
import com.example.recipes.model.Recipe

data class FavoriteRecipeListUiState(
    val favoriteRecipeList: List<Recipe> = emptyList(),
)

class FavoriteRecipeListViewModel() : ViewModel() {
    private val _favoriteRecipeListState = MutableLiveData(FavoriteRecipeListUiState())
    private val backendSingleton = BackendSingleton()
    val favoriteRecipeListState: LiveData<FavoriteRecipeListUiState> = _favoriteRecipeListState

    fun loadFavoriteRecipeList(ids: Set<Int>) {
        val favoriteRecipeList = backendSingleton.getRecipeListByIds(ids)
        val currentState = _favoriteRecipeListState.value ?: FavoriteRecipeListUiState()

        _favoriteRecipeListState.value = currentState.copy(
            favoriteRecipeList = favoriteRecipeList,
        )
    }
}