package com.example.recipes.ui.category

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipes.R
import com.example.recipes.databinding.ItemCategoryBinding
import com.example.recipes.model.Category

class CategoryListAdapter(var dataSet: List<Category>) :
    RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    inner class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemCategoryBinding.inflate(inflater, viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val category: Category = dataSet[position]

        viewHolder.binding.tvCategoryTitle.text = category.title
        viewHolder.binding.tvCategoryDescription.text = category.description

        try {
            Glide.with(viewHolder.itemView.context)
                .load(category.imageUrl)
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_error)
                .into(viewHolder.binding.ivCategoryImage)
        } catch (e: Exception) {
            Log.e("ImageLoadError", "Image not found: ${category.title}", e)
            null
        }

        viewHolder.binding.ivCategoryImage.contentDescription = "Изображение категории ${category.title}"
        viewHolder.binding.cvCategoryItem.setOnClickListener {
            itemClickListener?.onItemClick(category.id)
        }
    }

    override fun getItemCount() = dataSet.size
}