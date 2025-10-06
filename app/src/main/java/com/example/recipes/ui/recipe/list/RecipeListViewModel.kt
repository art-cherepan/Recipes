package com.example.recipes.ui.recipe.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipes.data.RecipesRepository
import com.example.recipes.model.Recipe

data class RecipeListUiState(
    val categoryId: Int? = null,
    val categoryName: String? = null,
    var categoryImageUrl: String? = null,
    var recipeList: List<Recipe> = emptyList(),
)

class RecipeListViewModel() : ViewModel() {

    companion object {
        const val DEFAULT_CATEGORY_HEADER_IMG_URL = "burger.png"
    }

    private val _recipeListState = MutableLiveData(RecipeListUiState())
    private val repository = RecipesRepository()
    val recipeListState: LiveData<RecipeListUiState> = _recipeListState

    fun loadRecipeList(categoryId: Int?, categoryName: String?, categoryImageUrl: String?) {
        try {
            val future = repository.getRecipeListByCategoryId(
                categoryId = categoryId ?: 0,
            )
            val response = future?.get()

            if (response == null) {
                _recipeListState.postValue(null)

                return
            }

            if (response.isSuccessful) {
                val recipeList = response.body() ?: emptyList()

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
            Log.e("RecipeListViewModel", "Ошибка загрузки рецептов в категорий $categoryName", e)
        }
    }
}