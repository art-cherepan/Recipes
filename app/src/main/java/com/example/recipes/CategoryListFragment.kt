package com.example.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.recipes.databinding.FragmentListCategoriesBinding
import models.BackendSingleton

class CategoryListFragment : Fragment() {
    private lateinit var binding: FragmentListCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListCategoriesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    private fun initRecycler() {
        val categoriesAdapter = CategoriesListAdapter(BackendSingleton().getCategories())
        binding.rvCategories.adapter = categoriesAdapter

        categoriesAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick() {
                openRecipesByCategoryId()
            }
        })
    }

    private fun openRecipesByCategoryId() {
        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipesListFragment>(R.id.mainContainer)
            addToBackStack(null)
        }
    }
}