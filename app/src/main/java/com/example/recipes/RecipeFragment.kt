package com.example.recipes

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.recipes.databinding.FragmentRecipeBinding
import com.example.recipes.models.Recipe
import com.google.android.material.divider.MaterialDividerItemDecoration
import androidx.core.content.edit
import com.example.recipes.RecipeListFragment.Companion.ARG_RECIPE

class RecipeFragment : Fragment() {
    private lateinit var binding: FragmentRecipeBinding
    private lateinit var recipe: Recipe
    private var isRecipeLiked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
                @Suppress("DEPRECATION") arguments?.getParcelable(ARG_RECIPE)
            }
        } ?: return

        val drawable = try {
            Drawable.createFromStream(
                context?.assets?.open(recipe.imageUrl), null
            )
        } catch (e: Exception) {
            Log.e("ImageLoadError", "Image not found: ${recipe.imageUrl}", e)
            null
        }

        binding.tvFragmentRecipeTitle.text = recipe.title
        binding.ivFragmentRecipeImageHeader.setImageDrawable(drawable)

        val favoriteRecipeIds: MutableSet<String> = getFavorites().toMutableSet()
        setIcHeartImage(favoriteRecipeIds)

        binding.ibIcHeart.setOnClickListener {
            val isLiked = toggleLike()
            val resId = if (isLiked) R.drawable.ic_heart else R.drawable.ic_heart_empty

            binding.ibIcHeart.setImageResource(resId)

            if (isLiked) {
                favoriteRecipeIds.add(recipe.id.toString())
                val immutableSet: Set<String> = favoriteRecipeIds

                saveFavorites(immutableSet)
            } else {
                favoriteRecipeIds.removeIf { it == recipe.id.toString() }
                val immutableSet: Set<String> = favoriteRecipeIds

                saveFavorites(immutableSet)
            }
        }
    }

    private fun initRecycler() {
        val dividerForIngredientsAdapter = getDividerForAdapter(binding.rvIngredient)
        val dividerForMethodAdapter = getDividerForAdapter(binding.rvMethod)

        val ingredientsAdapter = IngredientsAdapter(recipe.ingredients)
        binding.rvIngredient.adapter = ingredientsAdapter
        binding.rvIngredient.isNestedScrollingEnabled = false
        binding.rvIngredient.addItemDecoration(dividerForIngredientsAdapter)

        val methodAdapter = MethodAdapter(recipe.method)
        binding.rvMethod.adapter = methodAdapter
        binding.rvMethod.setHasFixedSize(false)
        binding.rvMethod.isNestedScrollingEnabled = false
        binding.rvMethod.addItemDecoration(dividerForMethodAdapter)

        binding.sbPortionCount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                ingredientsAdapter.updateIngredients(progress)
                binding.tvPortionCount.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun getDividerForAdapter(view: View): MaterialDividerItemDecoration {
        val divider = MaterialDividerItemDecoration(
            view.context,
            MaterialDividerItemDecoration.VERTICAL,
        ).apply {
            isLastItemDecorated = false
            dividerColor = context?.let {
                ContextCompat.getColor(
                    it,
                    R.color.material_divider_item_decoration_color,
                )
            }!!
        }

        return divider
    }

    private fun toggleLike(): Boolean {
        isRecipeLiked = !isRecipeLiked

        return isRecipeLiked
    }

    private fun isRecipeLiked(recipe: Recipe, likedIds: Set<String>): Boolean {
        return recipe.id.toString() in likedIds
    }

    private fun setIcHeartImage(recipeIds: Set<String>) {
        val isLiked = isRecipeLiked(recipe, recipeIds)
        isRecipeLiked = isLiked

        with(binding) {
            ibIcHeart.setImageResource(
                if (isLiked) R.drawable.ic_heart else R.drawable.ic_heart_empty
            )
        }
    }

    private fun saveFavorites(favoriteRecipeIds: Set<String>) {
        val sharedPreferences = context?.getSharedPreferences(
            Constants.FAVORITE_RECIPES_PREFERENCES,
            Context.MODE_PRIVATE,
        )
        sharedPreferences?.edit {
            putStringSet(
                Constants.FAVORITE_RECIPES_KEY,
                favoriteRecipeIds,
            )
            apply()
        }
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
}