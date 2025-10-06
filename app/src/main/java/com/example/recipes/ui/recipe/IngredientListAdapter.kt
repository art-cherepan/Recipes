package com.example.recipes.ui.recipe

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.databinding.ItemRecipeIngredientBinding
import com.example.recipes.model.Ingredient
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientListAdapter(var dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientListAdapter.ViewHolder>() {

    private var portionCount: Int = 1

    @SuppressLint("NotifyDataSetChanged")
    fun setPortionCount(portionCount: Int) {
        this.portionCount = portionCount
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemRecipeIngredientBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemRecipeIngredientBinding.inflate(inflater, viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val ingredient: Ingredient = dataSet[position]
        var totalQuantity: BigDecimal
        var displayQuantity: String

        viewHolder.binding.tvRecipeIngredientTitle.text = ingredient.description

        if (ingredient.quantity.toDoubleOrNull()?.toInt() != null) {    // с бэкенда может прилететь "по вкусу" или, например, "3.0"
            totalQuantity = BigDecimal(ingredient.quantity) * BigDecimal(portionCount)
            displayQuantity =
                totalQuantity.setScale(1, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
        } else {
            displayQuantity = ingredient.quantity
        }

        val ingredientQuantityAndUnitOfMeasure =
            "$displayQuantity ${ingredient.unitOfMeasure}"
        viewHolder.binding.tvRecipeIngredientQuantity.text = ingredientQuantityAndUnitOfMeasure
    }

    override fun getItemCount() = dataSet.size
}