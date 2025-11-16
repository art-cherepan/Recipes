package com.example.recipes.data

import android.util.Log
import com.example.recipes.model.Category
import com.example.recipes.model.CategoryListDao
import com.example.recipes.model.Recipe
import com.example.recipes.model.RecipeListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class RecipesRepository(
    private val recipeListDao: RecipeListDao,
    private val categoryListDao: CategoryListDao,
    private val recipeApiService: RecipeApiService,
) {
    companion object {
        const val BASE_URL = "https://recipes.androidsprint.ru/api/"
        const val BASE_IMAGE_URL = "https://recipes.androidsprint.ru/api/images/"
    }

    suspend fun getCategoryListFromCache(): List<Category> = withContext(Dispatchers.IO) {
        categoryListDao.getAll()
    }

    suspend fun insertAllCategories(categoryList: List<Category>) = withContext(Dispatchers.IO) {
        categoryListDao.insertAll(categoryList)
    }

    suspend fun getRecipeListByCategoryIdFromCache(categoryId: Int): List<Recipe> = withContext(Dispatchers.IO) {
        recipeListDao.getRecipeListByCategory(categoryId = categoryId)
    }

    suspend fun getFavoriteRecipeListFromCache(): List<Recipe> = withContext(Dispatchers.IO) {
        recipeListDao.getFavorites()
    }

    suspend fun insertAllRecipeList(recipeList: List<Recipe>) = withContext(Dispatchers.IO) {
        recipeListDao.insertAll(recipeList)
    }

    suspend fun getCategoryList(): Response<List<Category>>? = withContext(Dispatchers.IO) {
        try {
            recipeApiService.getCategoryList()
        } catch (e: Exception) {
            Log.e("ConnectionThreadException", "Ошибка: ${e.message}", e)

            return@withContext null
        }
    }

    suspend fun getRecipeListByCategoryId(categoryId: Int): Response<List<Recipe>>? = withContext(Dispatchers.IO) {
        try {
            recipeApiService.getRecipeListByCategoryId(id = categoryId)
        } catch (e: Exception) {
            Log.e("ConnectionThreadException", "Ошибка: ${e.message}", e)

            return@withContext null
        }
    }

    suspend fun getRecipeById(recipeId: Int): Response<Recipe>? = withContext(Dispatchers.IO) {
        try {
            recipeApiService.getRecipeById(id = recipeId)
        } catch (e: Exception) {
            Log.e("ConnectionThreadException", "Ошибка: ${e.message}", e)

            return@withContext null
        }
    }

    suspend fun getRecipeList(recipeIds: String): Response<List<Recipe>>? = withContext(Dispatchers.IO) {
        try {
            recipeApiService.getRecipeList(query = recipeIds)
        } catch (e: Exception) {
            Log.e("ConnectionThreadException", "Ошибка: ${e.message}", e)

            return@withContext null
        }
    }

    suspend fun getCategoryById(categoryId: Int): Response<Category>? = withContext(Dispatchers.IO) {
        try {
            recipeApiService.getCategoryById(id = categoryId)
        } catch (e: Exception) {
            Log.e("ConnectionThreadException", "Ошибка: ${e.message}", e)

            return@withContext null
        }
    }

    suspend fun updateFavorites(ids: List<Int>, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        recipeListDao.updateFavorites(ids, isFavorite)
    }

    suspend fun toggleFavorite(id: Int) = withContext(Dispatchers.IO) {
        recipeListDao.toggleFavorite(id)
    }
}