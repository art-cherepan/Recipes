package com.example.recipes.ui.recipe.favorite

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipes.data.RecipesRepository
import com.example.recipes.model.Recipe
import kotlinx.coroutines.launch

data class FavoriteRecipeListUiState(
    val favoriteRecipeList: List<Recipe> = emptyList(),
)

class FavoriteRecipeListViewModel(application: Application) : AndroidViewModel(application = application) {
    private val repository = RecipesRepository(context = application.applicationContext)
    private val _favoriteRecipeListState = MutableLiveData(FavoriteRecipeListUiState())
    val favoriteRecipeListState: LiveData<FavoriteRecipeListUiState> = _favoriteRecipeListState

    fun loadFavoriteRecipeList(ids: Set<Int>) {
        viewModelScope.launch {
            try {
                val favoriteRecipeListFromCache = repository.getFavoriteRecipeListFromCache()

                _favoriteRecipeListState.postValue(
                    FavoriteRecipeListUiState(favoriteRecipeList = favoriteRecipeListFromCache)
                )

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

                    val ids = recipeList.map { it.id }
                    repository.updateFavorites(ids, true)

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