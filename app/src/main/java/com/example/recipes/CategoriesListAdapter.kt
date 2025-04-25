package com.example.recipes

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.databinding.ItemCategoryBinding
import com.example.recipes.entity.Category

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick()
    }

    val itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        val itemClickListener = listener
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

        val drawable = try {
            Drawable.createFromStream(
                viewHolder.itemView.context.assets.open(category.imageUrl),
                null
            )
        } catch (e: Exception) {
            Log.e("ImageLoadError", "Image not found: ${category.title}", e)
            null
        }

        viewHolder.binding.ivCategoryImage.setImageDrawable(drawable)
        viewHolder.binding.ivCategoryImage.contentDescription = "Изображение категории ${category.title}"
        viewHolder.binding.categoryItemId.setOnClickListener { //не понимаю зачем мы вешаем слушателя в адаптере и во фрагменте...
            itemClickListener?.onItemClick()
        }
    }

    override fun getItemCount() = dataSet.size
}