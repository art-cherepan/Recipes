package com.example.recipes

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipes.databinding.FragmentRecipeBinding
import com.example.recipes.models.Recipe
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {
    private lateinit var binding: FragmentRecipeBinding
    private lateinit var recipe: Recipe

    companion object {
        const val ARG_RECIPE = "arg_recipe"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initRecycler()
    }

    private fun initUI() {
         recipe = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
            }
            else -> {
                @Suppress("DEPRECATION")
                arguments?.getParcelable(ARG_RECIPE) as? Recipe
            }
        } ?: return

        val drawable = try {
            Drawable.createFromStream(
                context?.assets?.open(recipe.imageUrl),
                null
            )
        } catch (e: Exception) {
            Log.e("ImageLoadError", "Image not found: ${recipe.imageUrl}", e)
            null
        }

        binding.tvFragmentRecipeTitle.text = recipe.title
        binding.ivFragmentRecipeImageHeader.setImageDrawable(drawable)
    }

    private fun initRecycler() {
        val dividerForIngredientsAdapter = MaterialDividerItemDecoration(
            binding.rvIngredient.context,
            MaterialDividerItemDecoration.VERTICAL,
        )

        val dividerForMethodAdapter = MaterialDividerItemDecoration(
            binding.rvMethod.context,
            MaterialDividerItemDecoration.VERTICAL,
        )

        val ingredientsAdapter = IngredientsAdapter(recipe.ingredients)
        binding.rvIngredient.adapter = ingredientsAdapter
        binding.rvIngredient.setHasFixedSize(false)
        binding.rvIngredient.isNestedScrollingEnabled = false
        binding.rvIngredient.addItemDecoration(dividerForIngredientsAdapter)

        val methodAdapter = MethodAdapter(recipe.method)
        binding.rvMethod.adapter = methodAdapter
        binding.rvMethod.setHasFixedSize(false)
        binding.rvMethod.isNestedScrollingEnabled = false
        binding.rvMethod.addItemDecoration(dividerForMethodAdapter)
    }
}