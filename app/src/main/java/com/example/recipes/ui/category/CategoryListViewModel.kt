package com.example.recipes.ui.category

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipes.data.RecipesRepository
import com.example.recipes.model.Category
import kotlinx.coroutines.launch

data class CategoryListUiState(
    val categoryList: List<Category> = emptyList()
)

class CategoryListViewModel() : ViewModel() {
    private val repository = RecipesRepository()
    private val _categoryListState = MutableLiveData(CategoryListUiState())
    val categoryListState: LiveData<CategoryListUiState> = _categoryListState

    fun loadCategoryList() {
        viewModelScope.launch {
            try {
                val response = repository.getCategoryList()

                if (response == null) {
                    _categoryListState.postValue(null)

                    return@launch
                }

                if (response.isSuccessful) {
                    val categoryList = response.body()?.map { category ->
                        category.copy(
                            imageUrl = RecipesRepository::BASE_IMAGE_URL.get() + category.imageUrl,
                        )
                    } ?: emptyList()

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
}
