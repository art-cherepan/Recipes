package com.example.recipes.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipes.R
import com.example.recipes.databinding.FragmentListCategoriesBinding
import com.example.recipes.model.Category

class CategoryListFragment : Fragment() {
    private lateinit var binding: FragmentListCategoriesBinding
    private val categoryListViewModel: CategoryListViewModel by viewModels()
    private var categoryList: List<Category> = emptyList()

    companion object {
        const val ARG_CATEGORY_ID = "arg_category_id"
        const val ARG_CATEGORY_NAME = "arg_category_name"
        const val ARG_CATEGORY_IMAGE_URL = "arg_category_image_url"
    }

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
        val category = categoryList.find { it.id == categoryId }
        val categoryName = category?.title ?: categoryList.first().title
        val categoryImageUrl = category?.imageUrl ?: categoryList.first().imageUrl

        val bundle = bundleOf(
            ARG_CATEGORY_ID to categoryId,
            ARG_CATEGORY_NAME to categoryName,
            ARG_CATEGORY_IMAGE_URL to categoryImageUrl,
        )

        findNavController().navigate(R.id.recipeListFragment, bundle)
    }
}