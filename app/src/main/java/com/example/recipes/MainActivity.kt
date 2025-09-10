package com.example.recipes

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.recipes.databinding.ActivityMainBinding
import com.example.recipes.model.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val threadPool: ExecutorService = Executors.newFixedThreadPool(THREAD_COUNT)

    companion object {
        const val GET_CATEGORIES_API_URL = "https://recipes.androidsprint.ru/api/category"
        const val GET_RECIPES_BY_CATEGORY_URL = "https://recipes.androidsprint.ru/api/category/"
        const val THREAD_COUNT = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val thread = Thread {
            try {
                val responseCategoryListBodyText = fetchData(GET_CATEGORIES_API_URL)

                val gson = Gson()
                val listType = object : TypeToken<List<Category>>() {}.type
                val categoryList = gson.fromJson<List<Category>>(responseCategoryListBodyText, listType)
                val categoryIds = categoryList.map { it.id }

                categoryIds.forEach { categoryId ->
                    threadPool.execute {
                        val recipes = fetchData("$GET_RECIPES_BY_CATEGORY_URL$categoryId/recipes")

                        Log.i("!!!", recipes)
                    }
                }
            } catch (e: Exception) {
                Log.e(
                    "ConnectionThreadException",
                    "Ошибка: ${e.message}",
                    e,
                )
            }
        }

        thread.start()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCategories.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_categoryListFragment)
        }

        binding.btnFavorites.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_favoriteRecipeListFragment)
        }
    }

    private fun fetchData(url: String): String {
        val url = URL(url)
        val connection = url.openConnection() as HttpURLConnection

        try {
            connection.connect()
        } catch (e: Exception) {
            Log.e(
                "ConnectionException",
                "Ошибка: ${e.message}",
                e,
            )
        }

        return connection.inputStream.bufferedReader().readText()
    }
}