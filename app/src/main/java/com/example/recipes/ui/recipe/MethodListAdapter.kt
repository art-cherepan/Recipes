package com.example.recipes.ui.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes.databinding.ItemRecipeMethodBinding

class MethodListAdapter(var dataSet: List<String>) :
    RecyclerView.Adapter<MethodListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRecipeMethodBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val binding = ItemRecipeMethodBinding.inflate(inflater, viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val method = dataSet[position]

        viewHolder.binding.tvRecipeMethodDescription.text = method
    }

    override fun getItemCount() = dataSet.size
}