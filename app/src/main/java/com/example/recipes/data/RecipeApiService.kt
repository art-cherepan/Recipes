package com.example.recipes.data

import com.example.recipes.model.Category
import com.example.recipes.model.Recipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {

    @GET("category")
    suspend fun getCategoryList(): Response<List<Category>>

    @GET("recipes")
    suspend fun getRecipeList(
        @Query("ids") query: String
    ): Response<List<Recipe>>

    @GET("recipe/{id}")
    suspend fun getRecipeById(@Path("id") id: Int): Response<Recipe>

    @GET("category/{id}")
    suspend fun getCategoryById(@Path("id") id: Int): Response<Category>

    @GET("category/{id}/recipes")
    suspend fun getRecipeListByCategoryId(@Path("id") id: Int): Response<List<Recipe>>
}