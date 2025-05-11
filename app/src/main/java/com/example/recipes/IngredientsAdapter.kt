package com.example.recipes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.databinding.ItemRecipeIngredientBinding
import com.example.recipes.models.Ingredient
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var quantity: Int = 1

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

        val totalQuantity = BigDecimal(ingredient.quantity) * BigDecimal(quantity)
        val displayQuantity =
            totalQuantity.setScale(1, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()

        val ingredientQuantityAndUnitOfMeasure =
            "$displayQuantity ${ingredient.unitOfMeasure}"
        viewHolder.binding.tvRecipeIngredientQuantity.text = ingredientQuantityAndUnitOfMeasure
    }

    override fun getItemCount() = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }
}