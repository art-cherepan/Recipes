package com.example.recipes.ui.recipe.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipes.data.RecipesRepository
import com.example.recipes.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoriteRecipeListUiState(
    val favoriteRecipeList: List<Recipe> = emptyList(),
)

@HiltViewModel
class FavoriteRecipeListViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
    ) : ViewModel() {

    private val _favoriteRecipeListState = MutableLiveData(FavoriteRecipeListUiState())
    val favoriteRecipeListState: LiveData<FavoriteRecipeListUiState> = _favoriteRecipeListState

    fun loadFavoriteRecipeList(ids: Set<Int>) {
        viewModelScope.launch {
            try {
                val favoriteRecipeListFromCache = recipesRepository.getFavoriteRecipeListFromCache()

                _favoriteRecipeListState.postValue(
                    FavoriteRecipeListUiState(favoriteRecipeList = favoriteRecipeListFromCache)
                )

                val response = recipesRepository.getRecipeList(recipeIds = ids.joinToString(separator = ","))

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
                    recipesRepository.updateFavorites(ids, true)

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