package com.example.recipes.di

import android.content.Context
import androidx.room.Room
import com.example.recipes.data.RecipeApiService
import com.example.recipes.data.RecipesRepository.Companion.BASE_URL
import com.example.recipes.db.AppDatabase
import com.example.recipes.db.MIGRATION_1_2
import com.example.recipes.db.MIGRATION_2_3
import com.example.recipes.model.CategoryListDao
import com.example.recipes.model.RecipeListDao
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class RecipeModule {

    private val ALL_MIGRATIONS = arrayOf(
        MIGRATION_1_2,
        MIGRATION_2_3,
    )

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "database-recipes",
        )
            .addMigrations(*ALL_MIGRATIONS)
            .build()

    @Provides
    fun provideCategoryListDao(appDatabase: AppDatabase): CategoryListDao = appDatabase.categoryListDao()

    @Provides
    fun provideRecipeListDao(appDatabase: AppDatabase): RecipeListDao = appDatabase.recipeListDao()

    @Provides
    fun provideHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()

        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()

        return retrofit
    }

    @Provides
    fun provideRecipeApiService(retrofit: Retrofit): RecipeApiService {
        return retrofit.create(RecipeApiService::class.java)
    }
}