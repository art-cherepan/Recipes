package com.example.recipes

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipes.databinding.FragmentListRecipesBinding
import models.BackendSingleton
import java.io.InputStream

class RecipeListFragment : Fragment() {
    private lateinit var binding: FragmentListRecipesBinding
    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

    companion object {
        const val ARG_CATEGORY_ID = "arg_category_id"
        const val ARG_CATEGORY_NAME = "arg_category_name"
        const val ARG_CATEGORY_IMAGE_URL = "arg_category_image_url"
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
        initBundleData()

        val drawable = Drawable.createFromStream(categoryImageUrl?.let {
            context?.assets?.open(it)
                ?: "burger.png"
        } as InputStream?, null)

        binding.ivRecipesImageHeader.setImageDrawable(drawable)
        binding.tvRecipesTitle.text = categoryName

        initRecycler()
    }

    private fun initBundleData() {
        arguments?.let { args ->
            categoryId = args.getInt(ARG_CATEGORY_ID)
            categoryName = args.getString(ARG_CATEGORY_NAME)
            categoryImageUrl = args.getString(ARG_CATEGORY_IMAGE_URL)
        } ?: throw IllegalStateException("Arguments must not be null")
    }

    private fun initRecycler() {
        val recipes = categoryId?.let { BackendSingleton().getRecipesByCategoryId(it) }
        val recipeListAdapter = recipes?.let { RecipeListAdapter(it) }
        binding.rvRecipes.adapter = recipeListAdapter

        recipeListAdapter?.setOnItemClickListener(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {

    }
}