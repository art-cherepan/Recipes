package com.example.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        categoriesAdapter.setOnItemClickListener(object: CategoriesListAdapter.OnItemClickListener { //не понимаю зачем мы вешаем слушателя в адаптере и во фрагменте...
            override fun onItemClick() {
                childFragmentManager.beginTransaction()
                    .replace(R.id.idFragmentListCategories, RecipesListFragment())
                    .commit()
            }
        })
    }
}