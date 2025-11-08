package com.example.recipes.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipes.model.Category
import com.example.recipes.model.CategoryListDao
import com.example.recipes.model.Recipe
import com.example.recipes.model.RecipeListDao
import com.example.recipes.model.converter.IngredientListConverter

@Database(
    entities = [Category::class, Recipe::class],
    version = 2,
    exportSchema = true,
)
@TypeConverters(IngredientListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryListDao(): CategoryListDao
    abstract fun recipeListDao(): RecipeListDao
}