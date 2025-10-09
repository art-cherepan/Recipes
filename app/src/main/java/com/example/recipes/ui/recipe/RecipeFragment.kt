package com.example.recipes.ui.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.recipes.R
import com.example.recipes.databinding.FragmentRecipeBinding
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {
    private lateinit var binding: FragmentRecipeBinding
    private val recipeViewModel: RecipeViewModel by viewModels()
    private val args: RecipeFragmentArgs by navArgs()

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

    @SuppressLint("NotifyDataSetChanged")
    private fun initUI() {
        val ingredientListAdapter = IngredientListAdapter(emptyList())
        val methodListAdapter = MethodListAdapter(emptyList())

        recipeViewModel.recipeState.observe(viewLifecycleOwner) { state ->
            binding.tvFragmentRecipeTitle.text = state.recipe?.title
            Glide.with(this)
                .load(state.imageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(binding.ivFragmentRecipeImageHeader)
            binding.tvPortionCount.text = state.portionCount.toString()
            ingredientListAdapter.setPortionCount(state.portionCount)
            ingredientListAdapter.dataSet = state.recipe?.ingredients ?: emptyList()
            methodListAdapter.dataSet = state.recipe?.method ?: emptyList()
            ingredientListAdapter.notifyDataSetChanged()
            methodListAdapter.notifyDataSetChanged()
        }

        recipeViewModel.loadRecipe(
            id = args.recipeId,
        )

        setIcHeartImage(
            recipeIds = recipeViewModel.getFavoriteRecipeList(),
            recipeId = args.recipeId,
        )

        binding.ibIcHeart.setOnClickListener {
            val isLiked = recipeViewModel.onFavoriteRecipeListClicked()
            val resId = if (isLiked == true) R.drawable.ic_heart else R.drawable.ic_heart_empty

            binding.ibIcHeart.setImageResource(resId)
        }

        val dividerForIngredientsAdapter = getDividerForAdapter(binding.rvIngredientList)
        val dividerForMethodAdapter = getDividerForAdapter(binding.rvMethodList)

        binding.rvIngredientList.adapter = ingredientListAdapter
        binding.rvIngredientList.isNestedScrollingEnabled = false
        binding.rvIngredientList.addItemDecoration(dividerForIngredientsAdapter)

        binding.rvMethodList.adapter = methodListAdapter
        binding.rvMethodList.setHasFixedSize(false)
        binding.rvMethodList.isNestedScrollingEnabled = false
        binding.rvMethodList.addItemDecoration(dividerForMethodAdapter)

        binding.sbPortionCount.setOnSeekBarChangeListener(
            PortionSeekBarListener {
                progress -> recipeViewModel.updatePortionCount(progress)
            }
        )
    }

    private fun getDividerForAdapter(view: View): MaterialDividerItemDecoration {
        val divider = MaterialDividerItemDecoration(
            view.context,
            MaterialDividerItemDecoration.VERTICAL,
        ).apply {
            isLastItemDecorated = false
            dividerColor = ContextCompat.getColor(
                requireContext(),
                R.color.material_divider_item_decoration_color,
            )
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

class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(
        seekBar: SeekBar?,
        progress: Int,
        fromUser: Boolean
    ) {
        onChangeIngredients(progress)
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {}

    override fun onStopTrackingTouch(p0: SeekBar?) {}
}