package com.example.recipes.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Entity(
    tableName = "recipe",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
        )
    ],
    indices = [Index("category_id")]
)
@Parcelize
@Serializable
data class Recipe (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "ingredient_list") val ingredients: List<Ingredient>,
    @ColumnInfo(name = "method") val method: List<String>,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "category_id") val categoryId: Int = 0,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false,
) : Parcelable

@Dao
interface RecipeListDao {
    @Query("SELECT * FROM recipe WHERE category_id = :categoryId")
    suspend fun getRecipeListByCategory(categoryId: Int): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insertAll(recipeList: List<Recipe>)

    @Query("DELETE FROM recipe")
    suspend fun  deleteAll()
}