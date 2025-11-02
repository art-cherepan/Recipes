package com.example.recipes.ui.category

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipes.data.RecipesRepository
import com.example.recipes.model.Category
import kotlinx.coroutines.launch

data class CategoryListUiState(
    val categoryList: List<Category> = emptyList()
)

class CategoryListViewModel(application: Application) : AndroidViewModel(application = application) {
    private val repository = RecipesRepository(application.applicationContext)
    private val _categoryListState = MutableLiveData(CategoryListUiState())
    val categoryListState: LiveData<CategoryListUiState> = _categoryListState

    fun loadCategoryList() {
        viewModelScope.launch {
            try {
                val categoryListFromCache = repository.getCategoryListFromCache()

                if (categoryListFromCache.count() > 0) {
                    _categoryListState.postValue(
                        CategoryListUiState(categoryList = categoryListFromCache)
                    )
                }

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

                    repository.insertAllCategories(categoryList)

                } else {
                    Log.e("CategoryListViewModel", "Ошибка: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("CategoryListViewModel", "Ошибка загрузки категорий рецептов", e)
            }
        }
    }
}
