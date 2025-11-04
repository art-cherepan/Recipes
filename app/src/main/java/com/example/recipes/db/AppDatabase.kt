package com.example.recipes.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipes.model.Category
import com.example.recipes.model.CategoryListDao

@Database(entities = [Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryListDao(): CategoryListDao
}