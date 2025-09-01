package com.example.recipes.ui.recipe.list

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipes.databinding.FragmentRecipeListBinding

class RecipeListFragment : Fragment() {
    private lateinit var binding: FragmentRecipeListBinding
    private val recipeListViewModel: RecipeListViewModel by viewModels()

    companion object {
        const val ARG_CATEGORY_ID = "arg_category_id"
        const val ARG_CATEGORY_NAME = "arg_category_name"
        const val ARG_CATEGORY_IMAGE_URL = "arg_category_image_url"
        const val ARG_RECIPE_ID = "arg_recipe_id"
        const val DEFAULT_CATEGORY_HEADER_IMG_URL = "burger.png"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        try {
            recipeListViewModel.loadRecipeList(
                categoryId = arguments?.getInt(ARG_CATEGORY_ID),
                categoryName = arguments?.getString(ARG_CATEGORY_NAME),
                categoryImageUrl = arguments?.getString(ARG_CATEGORY_IMAGE_URL)
            )
        } catch (_: RuntimeException) {
            throw IllegalStateException("Arguments must not be null")
        }

        val recipeListAdapter = RecipeListAdapter(emptyList())

        recipeListViewModel.recipeListState.observe(viewLifecycleOwner) { item ->
            val drawable = loadDrawableFromAssets(item.categoryImageUrl)
            binding.ivFragmentListRecipesImageHeader.setImageDrawable(drawable)
            binding.ivFragmentListRecipesImageHeader.contentDescription = "Изображение категории рецептов ${item.categoryName}"
            binding.tvFragmentListRecipesTitle.text = item.categoryName
            recipeListAdapter.dataSet = item.recipeList
        }

        binding.rvRecipeList.adapter = recipeListAdapter

        recipeListAdapter.setOnItemClickListener(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val action = RecipeListFragmentDirections
            .actionRecipeListFragmentToRecipeFragment(recipeId = recipeId)

        findNavController().navigate(action)
    }

    private fun loadDrawableFromAssets(imagePath: String?): Drawable? {
        return try {
            imagePath?.let {
                Drawable.createFromStream(
                    requireContext().assets.open(it),
                    null,
                )
            }
        } catch (e: Exception) {
            Log.e("ImageLoadError", "Image not found: $imagePath", e)
            null
        }
    }
}