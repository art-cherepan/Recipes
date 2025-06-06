package com.example.recipes.ui.recipe.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipes.Constants
import com.example.recipes.R
import com.example.recipes.ui.recipe.RecipeFragment
import com.example.recipes.ui.recipe.list.RecipeListAdapter
import com.example.recipes.ui.recipe.list.RecipeListFragment
import com.example.recipes.data.BackendSingleton
import com.example.recipes.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private val backendSingleton = BackendSingleton()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        val ids: Set<Int> = getFavorites().mapNotNull { it.toIntOrNull() }.toSet()
        val recipes = backendSingleton.getRecipesByIds(ids)
        val recipeListAdapter = RecipeListAdapter(recipes)
        binding.rvFavorites.adapter = recipeListAdapter

        recipeListAdapter.setOnItemClickListener(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun getFavorites(): Set<String> {
        val sharedPreferences = context?.getSharedPreferences(
            Constants.FAVORITE_RECIPES_PREFERENCES,
            Context.MODE_PRIVATE,
        )

        return sharedPreferences?.getStringSet(
            Constants.FAVORITE_RECIPES_KEY,
            emptySet()
        )?.toSet() ?: emptySet()
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = backendSingleton.getRecipeById(recipeId)
        val bundle = bundleOf(RecipeListFragment.Companion.ARG_RECIPE to recipe)

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            addToBackStack(null)
        }
    }
}