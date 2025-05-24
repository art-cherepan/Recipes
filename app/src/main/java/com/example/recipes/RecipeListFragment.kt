package com.example.recipes

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipes.databinding.FragmentListRecipesBinding

class RecipeListFragment : Fragment() {
    private lateinit var binding: FragmentListRecipesBinding
    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null
    private val backendSingleton = BackendSingleton()

    companion object {
        const val ARG_CATEGORY_ID = "arg_category_id"
        const val ARG_CATEGORY_NAME = "arg_category_name"
        const val ARG_CATEGORY_IMAGE_URL = "arg_category_image_url"
        const val ARG_RECIPE = "arg_recipe"
        const val DEFAULT_CATEGORY_HEADER_IMG_URL = "burger.png"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListRecipesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initRecycler()
    }

    private fun initUI() {
        initBundleData()

        if (categoryImageUrl == null) categoryImageUrl = DEFAULT_CATEGORY_HEADER_IMG_URL

        val drawable = try {
            Drawable.createFromStream(
                context?.assets?.open(categoryImageUrl!!),
                null
            )
        } catch (e: Exception) {
            Log.e("ImageLoadError", "Image not found: $categoryImageUrl", e)
            null
        }

        binding.ivFragmentListRecipesImageHeader.setImageDrawable(drawable)
        binding.ivFragmentListRecipesImageHeader.contentDescription = "Изображение категории рецептов $categoryName"
        binding.tvFragmentListRecipesTitle.text = categoryName
    }

    private fun initBundleData() {
        arguments?.let { args ->
            categoryId = args.getInt(ARG_CATEGORY_ID)
            categoryName = args.getString(ARG_CATEGORY_NAME)
            categoryImageUrl = args.getString(ARG_CATEGORY_IMAGE_URL)
        } ?: throw IllegalStateException("Arguments must not be null")
    }

    private fun initRecycler() {
        val recipes = categoryId?.let { backendSingleton.getRecipesByCategoryId(it) }
        val recipeListAdapter = recipes?.let { RecipeListAdapter(it) }
        binding.rvFragmentListRecipes.adapter = recipeListAdapter

        recipeListAdapter?.setOnItemClickListener(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = backendSingleton.getRecipeById(recipeId)
        val bundle = bundleOf(ARG_RECIPE to recipe)

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            addToBackStack(null)
        }
    }
}