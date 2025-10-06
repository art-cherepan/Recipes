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
        const val THREAD_COUNT = 10
    }

    fun getCategoryList(): Future<Response<List<Category>>>? {
        try {
            val categoryListCall: Call<List<Category>> = service.getCategoryList()

            return threadPool.submit<Response<List<Category>>> {
                categoryListCall.execute()
            }
        } catch (e: Exception) {
            Log.e("ConnectionThreadException", "Ошибка: ${e.message}", e)

            return null
        }
    }

    fun getRecipeListByCategoryId(categoryId: Int): Future<Response<List<Recipe>>>? {
        try {
            val recipeListByCategoryIdCall: Call<List<Recipe>> = service.getRecipeListByCategoryId(
                id = categoryId,
            )

            return threadPool.submit<Response<List<Recipe>>> {
                recipeListByCategoryIdCall.execute()
            }
        } catch (e: Exception) {
            Log.e("ConnectionThreadException", "Ошибка: ${e.message}", e)

            return null
        }
    }

    fun getRecipeById(recipeId: Int): Future<Response<Recipe>>? {
        try {
            val recipeCall: Call<Recipe> = service.getRecipeById(id = recipeId)

            return threadPool.submit<Response<Recipe>> {
                recipeCall.execute()
            }
        } catch (e: Exception) {
            Log.e("ConnectionThreadException", "Ошибка: ${e.message}", e)

            return null
        }
    }

    fun getRecipeList(recipeIds: String): Future<Response<List<Recipe>>>? {
        try {
            val recipeListCall: Call<List<Recipe>> = service.getRecipeList(query = recipeIds)

            return threadPool.submit<Response<List<Recipe>>> {
                recipeListCall.execute()
            }
        } catch (e: Exception) {
            Log.e("ConnectionThreadException", "Ошибка: ${e.message}", e)

            return null
        }
    }

    fun getCategoryById(categoryId: Int): Future<Response<Category>>? {
        try {
            val category: Call<Category> = service.getCategoryById(id = categoryId)

            return threadPool.submit<Response<Category>> {
                category.execute()
            }
        } catch (e: Exception) {
            Log.e("ConnectionThreadException", "Ошибка: ${e.message}", e)

            return null
        }
    }
}