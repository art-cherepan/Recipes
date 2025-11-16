package com.example.recipes.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.recipes.data.RecipeApiService
import com.example.recipes.data.RecipesRepository
import com.example.recipes.data.RecipesRepository.Companion.BASE_URL
import com.example.recipes.db.AppDatabase
import com.example.recipes.db.MIGRATION_1_2
import com.example.recipes.db.MIGRATION_2_3
import com.example.recipes.model.CategoryListDao
import com.example.recipes.model.RecipeListDao
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class AppContainer(context: Context) {
    private val ALL_MIGRATIONS = arrayOf(
        MIGRATION_1_2,
        MIGRATION_2_3,
    )

    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "database-recipes",
    )
        .addMigrations(*ALL_MIGRATIONS)
        .build()

    private val categoryListDao: CategoryListDao = db.categoryListDao()
    private val recipeListDao: RecipeListDao = db.recipeListDao()
    private val contentType = "application/json".toMediaType()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    private val recipeApiService: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    val repository = RecipesRepository(
        recipeListDao = recipeListDao,
        categoryListDao = categoryListDao,
        recipeApiService = recipeApiService,
    )

    val categoryListViewModelFactory = CategoryListViewModelFactory(repository)
    val favoriteRecipeListViewModelFactory = FavoriteRecipeListViewModelFactory(repository)
    val recipeListViewModelFactory = RecipeListViewModelFactory(repository)
    val recipeViewModelFactory = RecipeViewModelFactory(
        recipesRepository = repository,
        application = context as Application,
    )
}