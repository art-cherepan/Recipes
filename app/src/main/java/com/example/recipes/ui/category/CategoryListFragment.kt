package com.example.recipes.ui.category

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipes.databinding.FragmentCategoryListBinding
import com.example.recipes.model.Category
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryListFragment : Fragment() {
    private lateinit var binding: FragmentCategoryListBinding
    private var categoryList: List<Category> = emptyList()

    private val  categoryListViewModel: CategoryListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

    @SuppressLint("NotifyDataSetChanged")
    private fun initUI() {
        val categoryListAdapter = CategoryListAdapter(emptyList())

        categoryListViewModel.categoryListState.observe(viewLifecycleOwner) { state ->
            if (state == null) {
                Toast.makeText(
                    requireContext(),
                    "Ошибка получения данных",
                    Toast.LENGTH_SHORT,
                ).show()

                return@observe
            }

            categoryListAdapter.dataSet = state.categoryList
            categoryListAdapter.notifyDataSetChanged()
            categoryList = state.categoryList
        }

        categoryListViewModel.loadCategoryList()
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