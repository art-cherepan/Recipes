package com.example.recipes.ui.recipe.favorite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipes.Constants
import com.example.recipes.R
import com.example.recipes.ui.recipe.list.RecipeListAdapter
import com.example.recipes.ui.recipe.list.RecipeListFragment
import com.example.recipes.databinding.FragmentFavoritesBinding
import kotlin.getValue

class FavoriteRecipeListFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private val favoriteRecipeListViewModel: FavoriteRecipeListViewModel by viewModels()

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
        initUI()
    }

    private fun initUI() {
        val ids: Set<Int> = getFavoriteRecipeList().mapNotNull { it.toIntOrNull() }.toSet()
        favoriteRecipeListViewModel.loadFavoriteRecipeList(ids)

        val recipeListAdapter = RecipeListAdapter(emptyList())

        favoriteRecipeListViewModel.favoriteRecipeListState.observe(viewLifecycleOwner) { item ->
            recipeListAdapter.dataSet = item.favoriteRecipeList
        }

        binding.rvFavoriteRecipeList.adapter = recipeListAdapter

        recipeListAdapter.setOnItemClickListener(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun getFavoriteRecipeList(): Set<String> {
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
        val bundle = bundleOf(RecipeListFragment.Companion.ARG_RECIPE_ID to recipeId)

        findNavController().navigate(R.id.recipeFragment, bundle)
    }
}