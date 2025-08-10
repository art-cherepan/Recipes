package com.example.recipes.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipes.data.BackendSingleton
import com.example.recipes.model.Category

data class CategoryListUiState(
    val categoryList: List<Category> = emptyList()
)

class CategoryListViewModel() : ViewModel() {
    private val _categoryListState = MutableLiveData(CategoryListUiState())
    private val backendSingleton = BackendSingleton()
    val categoryListState: LiveData<CategoryListUiState> = _categoryListState

    fun loadCategoryList() {
        val categoryList = backendSingleton.getCategoryList()
        val currentState = _categoryListState.value ?: CategoryListUiState()

        _categoryListState.value = currentState.copy(
            categoryList = categoryList,
        )
    }
}