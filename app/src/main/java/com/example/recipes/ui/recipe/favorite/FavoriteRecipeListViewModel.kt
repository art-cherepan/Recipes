package com.example.recipes.ui.recipe.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipes.data.RecipesRepository
import com.example.recipes.model.Recipe
import kotlinx.coroutines.launch

data class FavoriteRecipeListUiState(
    val favoriteRecipeList: List<Recipe> = emptyList(),
)

class FavoriteRecipeListViewModel() : ViewModel() {
    private val repository = RecipesRepository()
    private val _favoriteRecipeListState = MutableLiveData(FavoriteRecipeListUiState())
    val favoriteRecipeListState: LiveData<FavoriteRecipeListUiState> = _favoriteRecipeListState

    fun loadFavoriteRecipeList(ids: Set<Int>) {
        viewModelScope.launch {
            try {
                val response = repository.getRecipeList(recipeIds = ids.joinToString(separator = ","))

                if (response == null) {
                    _favoriteRecipeListState.postValue(null)

                    return@launch
                }

                if (response.isSuccessful) {
                    val recipeList = response.body()?.map { recipe ->
                        recipe.copy(
                            imageUrl = RecipesRepository::BASE_IMAGE_URL.get() + recipe.imageUrl,
                        )
                    } ?: emptyList()

                    _favoriteRecipeListState.postValue(
                        FavoriteRecipeListUiState(favoriteRecipeList = recipeList)
                    )
                } else {
                    Log.e("CategoryListViewModel", "Ошибка: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("FavoriteRecipeListViewModel", "Ошибка загрузки списка рецептов", e)
            }
        }
    }
}