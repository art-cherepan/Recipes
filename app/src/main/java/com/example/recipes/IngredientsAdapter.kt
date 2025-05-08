package com.example.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.databinding.ItemRecipeIngredientBinding
import com.example.recipes.models.Ingredient

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRecipeIngredientBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int,
    ): IngredientsAdapter.ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemRecipeIngredientBinding.inflate(inflater, viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: IngredientsAdapter.ViewHolder, position: Int) {
        val ingredient: Ingredient = dataSet[position]

        viewHolder.binding.tvRecipeIngredientTitle.text = ingredient.description

        val ingredientQuantityAndUnitOfMeasure = "${ingredient.quantity} ${ingredient.unitOfMeasure}"
        viewHolder.binding.tvRecipeIngredientQuantity.text = ingredientQuantityAndUnitOfMeasure
    }

    override fun getItemCount() = dataSet.size
}