package com.example.recipes.data

import android.util.Log
import com.example.recipes.model.Category
import com.example.recipes.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class RecipesRepository {
    private val threadPool: ExecutorService = Executors.newFixedThreadPool(THREAD_COUNT)
    private val contentType = "application/json".toMediaType()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    private val service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    companion object {
        const val BASE_URL = "https://recipes.androidsprint.ru/api/"
        const val BASE_IMAGE_URL = "https://recipes.androidsprint.ru/api/images/"
        const val THREAD_COUNT = 10
    }

    suspend fun getCategoryList(): Response<List<Category>>? = withContext(Dispatchers.IO) {
        try {
            val call = service.getCategoryList()
            call.execute()
        } catch (e: Exception) {
            Log.e("ConnectionThreadException", "Ошибка: ${e.message}", e)

            return@withContext null
        }
    }

    suspend fun getRecipeListByCategoryId(categoryId: Int): Response<List<Recipe>>? = withContext(Dispatchers.IO) {
        try {
            val call = service.getRecipeListByCategoryId(id = categoryId)
            call.execute()
        } catch (e: Exception) {
            Log.e("ConnectionThreadException", "Ошибка: ${e.message}", e)

            return@withContext null
        }
    }

    suspend fun getRecipeById(recipeId: Int): Response<Recipe>? = withContext(Dispatchers.IO) {
        try {
            val call = service.getRecipeById(id = recipeId)
            call.execute()
        } catch (e: Exception) {
            Log.e("ConnectionThreadException", "Ошибка: ${e.message}", e)

            return@withContext null
        }
    }

    suspend fun getRecipeList(recipeIds: String): Response<List<Recipe>>? = withContext(Dispatchers.IO) {
        try {
            val call = service.getRecipeList(query = recipeIds)
            call.execute()
        } catch (e: Exception) {
            Log.e("ConnectionThreadException", "Ошибка: ${e.message}", e)

            return@withContext null
        }
    }

    suspend fun getCategoryById(categoryId: Int): Response<Category>? = withContext(Dispatchers.IO) {
        try {
            val call = service.getCategoryById(id = categoryId)
            call.execute()
        } catch (e: Exception) {
            Log.e("ConnectionThreadException", "Ошибка: ${e.message}", e)

            return@withContext null
        }
    }
}