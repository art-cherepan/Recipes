package com.example.recipes.ui.category

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipes.data.RecipesRepository
import com.example.recipes.model.Category

data class CategoryListUiState(
    val categoryList: List<Category> = emptyList()
)

class CategoryListViewModel() : ViewModel() {
    private val repository = RecipesRepository()
    private val _categoryListState = MutableLiveData(CategoryListUiState())
    val categoryListState: LiveData<CategoryListUiState> = _categoryListState

    fun loadCategoryList() {
        try {
            val future = repository.getCategoryList()
            val response = future?.get()

            if (response == null) {
                _categoryListState.postValue(null)

                return
            }

            if (response.isSuccessful) {
                val categoryList = response.body() ?: emptyList()
                _categoryListState.postValue(
                    CategoryListUiState(categoryList = categoryList)
                )
            } else {
                Log.e("CategoryListViewModel", "Ошибка: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("CategoryListViewModel", "Ошибка загрузки категорий рецептов", e)
        }
    }
}
