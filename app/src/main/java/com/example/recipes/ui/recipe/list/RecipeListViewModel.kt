package com.example.recipes.ui.recipe.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipes.data.BackendSingleton
import com.example.recipes.model.Recipe
import com.example.recipes.ui.recipe.list.RecipeListFragment.Companion.DEFAULT_CATEGORY_HEADER_IMG_URL

data class RecipeListUiState(
    val categoryId: Int? = null,
    val categoryName: String? = null,
    var categoryImageUrl: String? = null,
    var recipeList: List<Recipe> = emptyList(),
)

class RecipeListViewModel() : ViewModel() {
    private val _recipeListState = MutableLiveData(RecipeListUiState())
    private val backendSingleton = BackendSingleton()
    val recipeListState: LiveData<RecipeListUiState> = _recipeListState

    fun loadRecipeList(categoryId: Int?, categoryName: String?, categoryImageUrl: String?) {
        val currentState = _recipeListState.value ?: RecipeListUiState()

        var categoryImageUrlUiState = categoryImageUrl
        if (categoryImageUrl == null) categoryImageUrlUiState = DEFAULT_CATEGORY_HEADER_IMG_URL

        val safeCategoryId = categoryId ?: 0
        val recipeList = backendSingleton.getRecipeListByCategoryId(safeCategoryId)

        _recipeListState.value = currentState.copy(
            categoryId = categoryId,
            categoryName = categoryName,
            categoryImageUrl = categoryImageUrlUiState,
            recipeList = recipeList,
        )
    }
}