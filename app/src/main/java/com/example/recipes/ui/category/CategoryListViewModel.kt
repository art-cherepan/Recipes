package com.example.recipes.ui.category

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipes.data.RecipesRepository
import com.example.recipes.model.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryListUiState(
    val categoryList: List<Category> = emptyList()
)

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
) : ViewModel() {

    private val _categoryListState = MutableLiveData(CategoryListUiState())
    val categoryListState: LiveData<CategoryListUiState> = _categoryListState

    fun loadCategoryList() {
        viewModelScope.launch {
            try {
                val categoryListFromCache = recipesRepository.getCategoryListFromCache()

                _categoryListState.postValue(
                    CategoryListUiState(categoryList = categoryListFromCache)
                )

                val response = recipesRepository.getCategoryList()

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

                    recipesRepository.insertAllCategories(categoryList)
                } else {
                    Log.e("CategoryListViewModel", "Ошибка: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("CategoryListViewModel", "Ошибка загрузки категорий рецептов", e)
            }
        }
    }
}
