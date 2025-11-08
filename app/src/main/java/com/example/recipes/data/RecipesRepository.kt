package com.example.recipes.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.recipes.db.AppDatabase
import com.example.recipes.db.MIGRATION_1_2
import com.example.recipes.model.Category
import com.example.recipes.model.CategoryListDao
import com.example.recipes.model.Recipe
import com.example.recipes.model.RecipeListDao
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit

class RecipesRepository(context: Context) {
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "database-recipes",
    )
        .addMigrations(MIGRATION_1_2)
        .build()

    private val categoryListDao: CategoryListDao = db.categoryListDao()
    private val recipeListDao: RecipeListDao = db.recipeListDao()
    private val contentType = "application/json".toMediaType()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

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

    suspend fun insertAllRecipeList(recipeList: List<Recipe>) = withContext(Dispatchers.IO) {
        recipeListDao.insertAll(recipeList)
    }

    suspend fun getCategoryList(): Response<List<Category>>? = withContext(Dispatchers.IO) {
        try {
            service.getCategoryList()
        } catch (e: Exception) {
            Log.e("ConnectionThreadException", "Ошибка: ${e.message}", e)

            return@withContext null
        }
    }

    suspend fun getRecipeListByCategoryId(categoryId: Int): Response<List<Recipe>>? = withContext(Dispatchers.IO) {
        try {
            service.getRecipeListByCategoryId(id = categoryId)
        } catch (e: Exception) {
            Log.e("ConnectionThreadException", "Ошибка: ${e.message}", e)

            return@withContext null
        }
    }

    suspend fun getRecipeById(recipeId: Int): Response<Recipe>? = withContext(Dispatchers.IO) {
        try {
            service.getRecipeById(id = recipeId)
        } catch (e: Exception) {
            Log.e("ConnectionThreadException", "Ошибка: ${e.message}", e)

            return@withContext null
        }
    }

    suspend fun getRecipeList(recipeIds: String): Response<List<Recipe>>? = withContext(Dispatchers.IO) {
        try {
            service.getRecipeList(query = recipeIds)
        } catch (e: Exception) {
            Log.e("ConnectionThreadException", "Ошибка: ${e.message}", e)

            return@withContext null
        }
    }

    suspend fun getCategoryById(categoryId: Int): Response<Category>? = withContext(Dispatchers.IO) {
        try {
            service.getCategoryById(id = categoryId)
        } catch (e: Exception) {
            Log.e("ConnectionThreadException", "Ошибка: ${e.message}", e)

            return@withContext null
        }
    }
}