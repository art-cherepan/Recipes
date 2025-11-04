package com.example.recipes.ui.recipe.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipes.data.RecipesRepository
import com.example.recipes.model.Recipe
import kotlinx.coroutines.launch

data class RecipeListUiState(
    val categoryId: Int? = null,
    val categoryName: String? = null,
    var categoryImageUrl: String? = null,
    var recipeList: List<Recipe> = emptyList(),
)

class RecipeListViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val DEFAULT_CATEGORY_HEADER_IMG_URL = "burger.png"
    }

    private val _recipeListState = MutableLiveData(RecipeListUiState())
    private val repository = RecipesRepository(context = application.applicationContext)
    val recipeListState: LiveData<RecipeListUiState> = _recipeListState

    fun loadRecipeList(categoryId: Int?, categoryName: String?, categoryImageUrl: String?) {
        viewModelScope.launch {
            try {
                val response = repository.getRecipeListByCategoryId(
                    categoryId = categoryId ?: 0,
                )

                if (response == null) {
                    _recipeListState.postValue(null)

                    return@launch
                }

                if (response.isSuccessful) {
                    val recipeList = response.body()?.map { recipe ->
                        recipe.copy(
                            imageUrl = RecipesRepository::BASE_IMAGE_URL.get() + recipe.imageUrl,
                        )
                    } ?: emptyList()

                    _recipeListState.postValue(
                        RecipeListUiState(
                            categoryId = categoryId,
                            categoryName = categoryName,
                            categoryImageUrl = categoryImageUrl ?: DEFAULT_CATEGORY_HEADER_IMG_URL,
                            recipeList = recipeList,
                        )
                    )
                } else {
                    Log.e("RecipeListViewModel", "Ошибка: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e(
                    "RecipeListViewModel",
                    "Ошибка загрузки рецептов в категорий $categoryName",
                    e
                )
            }
        }
    }
}