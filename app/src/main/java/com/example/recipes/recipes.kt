package com.example.recipes

import com.example.recipes.entity.Category

object STUB {
    private val categories = listOf(
        Category(
            id = 0,
            title = "Бургеры",
            description = "Рецепты всех популярных видов бургеров",
            imageUrl = "app/src/main/assets/burger.png",
        ),
        Category(
            id = 1,
            title = "Десерты",
            description = "Самые вкусные рецепты десертов специально для вас",
            imageUrl = "app/src/main/assets/dessert.png",
        ),
        Category(
            id = 2,
            title = "Пицца",
            description = "Пицца на любой вкус и цвет. Лучшая подборка для тебя",
            imageUrl = "app/src/main/assets/pizza.png",
        ),
        Category(
            id = 3,
            title = "Рыба",
            description = "Печеная, жареная, сушеная, любая рыба на твой вкус",
            imageUrl = "app/src/main/assets/fish.png",
        ),
        Category(
            id = 4,
            title = "Супы",
            description = "От классики до экзотики: мир в одной тарелке",
            imageUrl = "app/src/main/assets/soup.png",
        ),
        Category(
            id = 5,
            title = "Салаты",
            description = "Хрустящий калейдоскоп под соусом вдохновения",
            imageUrl = "app/src/main/assets/salad.png",
        ),
    )

    fun getCategories(): List<Category> {
        return categories
    }
}
