package com.example.recipes

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipes.databinding.FragmentRecipeBinding
import com.example.recipes.models.Recipe

class RecipeFragment : Fragment() {
    private lateinit var binding: FragmentRecipeBinding

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

        val recipe = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
            }
            else -> {
                @Suppress("DEPRECATION")
                arguments?.getParcelable(ARG_RECIPE) as? Recipe
            }
        } ?: return

        binding.tvFragmentRecipe.text = recipe.title
    }
}