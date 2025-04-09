package com.example.recipes.entity

data class Recipe (
    val id: Int,
    val title: String,
    val ingredients: List<Ingredient>,
)