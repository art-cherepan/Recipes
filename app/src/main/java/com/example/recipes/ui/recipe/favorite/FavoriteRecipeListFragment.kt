package com.example.recipes.ui.recipe.favorite

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recipes.Constants
import com.example.recipes.RecipesApplication
import com.example.recipes.databinding.FragmentFavoriteListBinding
import com.example.recipes.ui.recipe.list.RecipeListAdapter

class FavoriteRecipeListFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteListBinding
    private lateinit var favoriteRecipeListViewModel: FavoriteRecipeListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireActivity().application as RecipesApplication).appContainer
        favoriteRecipeListViewModel = appContainer.favoriteRecipeListViewModelFactory.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initUI() {
        val ids: Set<Int> = getFavoriteRecipeList().mapNotNull { it.toIntOrNull() }.toSet()
        val recipeListAdapter = RecipeListAdapter(emptyList())

        favoriteRecipeListViewModel.favoriteRecipeListState.observe(viewLifecycleOwner) { state ->
            if (state == null) {
                Toast.makeText(
                    requireContext(),
                    "Ошибка получения данных",
                    Toast.LENGTH_SHORT,
                ).show()

                return@observe
            }

            recipeListAdapter.dataSet = state.favoriteRecipeList
            recipeListAdapter.notifyDataSetChanged()
        }

        favoriteRecipeListViewModel.loadFavoriteRecipeList(ids)

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
        val action = FavoriteRecipeListFragmentDirections
            .actionFavoriteRecipeListFragmentToRecipeFragment(recipeId = recipeId)

        findNavController().navigate(action)
    }
}