package com.example.recipes.ui.recipe.list

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipes.R
import com.example.recipes.databinding.FragmentRecipeListBinding

class RecipeListFragment : Fragment() {
    private lateinit var binding: FragmentRecipeListBinding
    private val recipeListViewModel: RecipeListViewModel by viewModels()
    private val args: RecipeListFragmentArgs by navArgs()

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

    @SuppressLint("NotifyDataSetChanged")
    private fun initUI() {
        val recipeListAdapter = RecipeListAdapter(emptyList())

        try {
            recipeListViewModel.loadRecipeList(
                categoryId = args.category.id,
                categoryName = args.category.title,
                categoryImageUrl = args.category.imageUrl,
            )
        } catch (_: RuntimeException) {
            throw IllegalStateException("Arguments must not be null")
        }

        recipeListViewModel.recipeListState.observe(viewLifecycleOwner) { state ->
            if (state == null) {
                Toast.makeText(
                    requireContext(),
                    "Ошибка получения данных",
                    Toast.LENGTH_SHORT,
                ).show()

                return@observe
            }

            Glide.with(this)
                .load(state.categoryImageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.ivFragmentListRecipesImageHeader)

            binding.ivFragmentListRecipesImageHeader.contentDescription = "Изображение категории рецептов ${state.categoryName}"
            binding.tvFragmentListRecipesTitle.text = state.categoryName
            recipeListAdapter.dataSet = state.recipeList
            recipeListAdapter.notifyDataSetChanged()
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