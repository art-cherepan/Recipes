package com.example.recipes.ui.recipe.list

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.databinding.ItemRecipeBinding
import com.example.recipes.model.Recipe

class RecipeListAdapter(private val dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    inner class ViewHolder(
        val binding: ItemRecipeBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemRecipeBinding.inflate(inflater, viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val recipe: Recipe = dataSet[position]

        viewHolder.binding.tvRecipeTitle.text = recipe.title

        val drawable = try {
            Drawable.createFromStream(
                viewHolder.itemView.context.assets.open(recipe.imageUrl),
                null
            )
        } catch (e: Exception) {
            Log.e("ImageLoadError", "Image not found: ${recipe.title}", e)
            null
        }

        viewHolder.binding.ivRecipeImage.setImageDrawable(drawable)
        viewHolder.binding.ivRecipeImage.contentDescription = "Изображение рецепта ${recipe.title}"
        viewHolder.binding.cvRecipeItem.setOnClickListener {
            itemClickListener?.onItemClick(recipe.id)
        }
    }

    override fun getItemCount() = dataSet.size
}