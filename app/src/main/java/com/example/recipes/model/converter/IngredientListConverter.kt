package com.example.recipes.model.converter

import androidx.room.TypeConverter
import com.example.recipes.model.Ingredient
import kotlinx.serialization.json.Json

class IngredientListConverter {
    private val json = Json { ignoreUnknownKeys = true }

    // --- Для списка ингредиентов ---
    @TypeConverter
    fun fromIngredientList(value: List<Ingredient>?): String {
        return json.encodeToString(value ?: emptyList())
    }

    @TypeConverter
    fun toIngredientList(value: String): List<Ingredient> {
        return json.decodeFromString(value)
    }

    // --- Для списка методов ---
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return json.encodeToString(value ?: emptyList())
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return json.decodeFromString(value)
    }
}