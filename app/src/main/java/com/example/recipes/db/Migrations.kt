package com.example.recipes.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS recipe (
                id INTEGER PRIMARY KEY NOT NULL,
                title TEXT NOT NULL,
                ingredient_list TEXT NOT NULL,
                method TEXT NOT NULL,
                image_url TEXT NOT NULL,
                category_id INTEGER NOT NULL DEFAULT 0,
                FOREIGN KEY(category_id) REFERENCES category(id)
            )
            """.trimIndent()
        )
        db.execSQL("CREATE INDEX IF NOT EXISTS index_recipe_category_id ON recipe(category_id)")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE recipe ADD COLUMN is_favorite INTEGER NOT NULL DEFAULT 0")
    }
}