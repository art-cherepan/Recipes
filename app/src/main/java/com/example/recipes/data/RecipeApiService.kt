package com.example.recipes.data

import com.example.recipes.model.Category
import com.example.recipes.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {

    @GET("category")
    fun getCategoryList(): Call<List<Category>>

    @GET("recipes")
    fun getRecipeList(
        @Query("ids") query: String
    ): Call<List<Recipe>>

    @GET("recipe/{id}")
    fun getRecipeById(@Path("id") id: Int): Call<Recipe>

    @GET("category/{id}")
    fun getCategoryById(@Path("id") id: Int): Call<Category>

    @GET("category/{id}/recipes")
    fun getRecipeListByCategoryId(@Path("id") id: Int): Call<List<Recipe>>
}