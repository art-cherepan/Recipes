package com.example.recipes.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.recipes.R
import com.example.recipes.databinding.FragmentRecipeBinding
import com.example.recipes.ui.recipe.list.RecipeListFragment
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {
    private lateinit var binding: FragmentRecipeBinding
    private val recipeUiStateModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        recipeUiStateModel.recipeState.observe(viewLifecycleOwner) {
            item ->
                binding.tvFragmentRecipeTitle.text = item.recipe?.title
                binding.ivFragmentRecipeImageHeader.setImageDrawable(item.recipeImage)
        }

        recipeUiStateModel.loadRecipe(
            arguments?.getInt(RecipeListFragment.Companion.ARG_RECIPE_ID),
        )

        setIcHeartImage(
            recipeIds = recipeUiStateModel.getFavorites(),
            recipeId = arguments?.getInt(RecipeListFragment.Companion.ARG_RECIPE_ID)
        )

        binding.ibIcHeart.setOnClickListener {
            val isLiked = recipeUiStateModel.onFavoritesClicked()
            val resId = if (isLiked == true) R.drawable.ic_heart else R.drawable.ic_heart_empty

            binding.ibIcHeart.setImageResource(resId)
        }

        val dividerForIngredientsAdapter = getDividerForAdapter(binding.rvIngredient)
        val dividerForMethodAdapter = getDividerForAdapter(binding.rvMethod)

        val ingredientsAdapter = IngredientsAdapter(recipeUiStateModel.recipeState.value!!.recipe!!.ingredients)
        ingredientsAdapter.setQuantity(recipeUiStateModel.recipeState.value!!.portionCount)
        binding.rvIngredient.adapter = ingredientsAdapter
        binding.rvIngredient.isNestedScrollingEnabled = false
        binding.rvIngredient.addItemDecoration(dividerForIngredientsAdapter)

        val methodAdapter = MethodAdapter(recipeUiStateModel.recipeState.value!!.recipe!!.method)
        binding.rvMethod.adapter = methodAdapter
        binding.rvMethod.setHasFixedSize(false)
        binding.rvMethod.isNestedScrollingEnabled = false
        binding.rvMethod.addItemDecoration(dividerForMethodAdapter)

        binding.sbPortionCount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                ingredientsAdapter.updateIngredients(progress)
                recipeUiStateModel.updatePortionCount(progress)
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

    private fun setIcHeartImage(recipeIds: Set<String>, recipeId: Int?) {
        val isLiked = recipeId.toString() in recipeIds

        with(binding) {
            ibIcHeart.setImageResource(
                if (isLiked) R.drawable.ic_heart else R.drawable.ic_heart_empty
            )
        }
    }
}