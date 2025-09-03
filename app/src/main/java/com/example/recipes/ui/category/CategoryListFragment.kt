package com.example.recipes.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipes.databinding.FragmentCategoryListBinding
import com.example.recipes.model.Category

class CategoryListFragment : Fragment() {
    private lateinit var binding: FragmentCategoryListBinding
    private val categoryListViewModel: CategoryListViewModel by viewModels()
    private var categoryList: List<Category> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        categoryListViewModel.loadCategoryList()

        val categoryListAdapter = CategoryListAdapter(emptyList())

        categoryListViewModel.categoryListState.observe(viewLifecycleOwner) { item ->
            categoryListAdapter.dataSet = item.categoryList
            categoryList = item.categoryList
        }

        binding.rvCategoryList.adapter = categoryListAdapter

        categoryListAdapter.setOnItemClickListener(object :
            CategoryListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipeListByCategoryId(categoryId)
            }
        })
    }

    private fun openRecipeListByCategoryId(categoryId: Int) {
        val category = categoryList.find { it.id == categoryId } ?: throw IllegalArgumentException()

        val action = CategoryListFragmentDirections
            .actionCategoryListFragmentToRecipeListFragment(category = category)

        findNavController().navigate(action)
    }
}