package com.example.recipes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.databinding.ItemRecipeIngredientBinding
import com.example.recipes.models.Ingredient

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var quantity: Int = 0

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

        val commonQuantityToString = (ingredient.quantity.toDouble() * quantity).toString()

        val ingredientQuantityAndUnitOfMeasure =
            "${formatNumber(commonQuantityToString)} ${ingredient.unitOfMeasure}"
        viewHolder.binding.tvRecipeIngredientQuantity.text = ingredientQuantityAndUnitOfMeasure
    }

    override fun getItemCount() = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }

    private fun Double.isWhole() = this % 1 == 0.0

    private fun formatNumber(input: String): String {
        val cleanInput = input.replace(',', '.')
        val number = cleanInput.toDoubleOrNull() ?: return "Ошибка: аргумент не является числом"

        return if (number.isWhole()) {
            number.toInt().toString()
        } else {
            "%.1f".format(number)
        }
    }
}