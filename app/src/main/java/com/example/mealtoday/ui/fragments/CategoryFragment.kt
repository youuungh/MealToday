package com.example.mealtoday.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mealtoday.R
import com.example.mealtoday.adapters.CategoryAdapter
import com.example.mealtoday.databinding.FragmentCategoryBinding
import com.example.mealtoday.ui.fragments.bottomSheetDialog.CategoryBottomSheet
import com.example.mealtoday.viewModel.CategoryViewModel
import com.google.android.material.transition.platform.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category) {

    private val categoryViewModel: CategoryViewModel by viewModels()
    private val args: CategoryFragmentArgs by navArgs()

    private lateinit var navController: NavController
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).addTarget(view)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).addTarget(view)
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        binding.back.setOnClickListener { navController.popBackStack() }
        binding.categoryTitle.text = args.categoryName

        categoryAdapter = CategoryAdapter()

        setUpCategory()
    }

    private fun setUpCategory() {
        lifecycleScope.launch {
            categoryViewModel.getCategory(args.categoryName)
            categoryViewModel.categoryStateFlow.collect { data ->
                categoryAdapter.differ.submitList(data)
            }
        }

        with(binding.categoryContentRecycler) {
            //layoutManager = LinearLayoutManager(context)
            layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
            //layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = categoryAdapter
            setHasFixedSize(true)
        }

        categoryAdapter.onCategoryItemClick = { data ->
            CategoryBottomSheet().apply {
                setData(data)
            }.show(childFragmentManager, "CategoryBottomSheet")
        }
    }
}