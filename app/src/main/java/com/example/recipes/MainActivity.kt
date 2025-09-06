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

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val GET_CATEGORIES_API_URL = "https://recipes.androidsprint.ru/api/category"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        val thread = Thread {
            try {
                val url = URL(GET_CATEGORIES_API_URL)
                val connection = url.openConnection() as HttpURLConnection

                connection.connect()

                val responseBodyText = connection.inputStream.bufferedReader().readText()

                Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
                Log.i("!!!", "Body: $responseBodyText")

                val gson = Gson()
                val listType = object : TypeToken<List<Category>>() {}.type
                val categoryList = gson.fromJson<List<Category>>(responseBodyText, listType)

                categoryList.forEach {
                    Log.i("!!!", "Название категории рецептов: ${it.title}")
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
}