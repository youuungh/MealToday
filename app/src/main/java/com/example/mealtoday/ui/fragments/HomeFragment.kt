package com.example.mealtoday.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealtoday.R
import com.example.mealtoday.adapters.CategoriesHomeAdapter
import com.example.mealtoday.adapters.HotAdapter
import com.example.mealtoday.databinding.FragmentHomeBinding
import com.example.mealtoday.utils.doOnApplyWindowInsets
import com.example.mealtoday.viewModel.HomeViewModel
import com.google.android.material.transition.platform.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var hotAdapter : HotAdapter
    private lateinit var categoriesHomeAdapter : CategoriesHomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterTransition = MaterialFadeThrough().addTarget(binding.contentContainer)
        reenterTransition = MaterialFadeThrough().addTarget(binding.contentContainer)

        hotAdapter = HotAdapter()
        categoriesHomeAdapter = CategoriesHomeAdapter()

        getRandomMeal()
        getHotMeal()
        setUpHotRecyclerView()
        getCategories()
        setUpCategoriesRecyclerView()

        requireView().doOnApplyWindowInsets { insetView, windowInsets, initialPadding, _ ->
            insetView.updatePadding(
                top = initialPadding.top + windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
            )
        }
    }
    private fun getRandomMeal() {
        homeViewModel.getRandomMeal()
        homeViewModel.getRandomMealLiveData.observe(viewLifecycleOwner) { data ->
            run {
                Glide.with(this)
                    .load(data.strMealThumb)
                    .into(binding.randomImage)

                try {
                    binding.cvRandomImage.setOnClickListener {
                        val extras = FragmentNavigatorExtras(binding.cvRandomImage to "randomImage")
                        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMealFragment(
                            data.idMeal, data.strMealThumb, data.strMeal), extras
                        )
                    }
                } catch (t:Throwable) {
                    Log.d("TAG", t.message.toString())
                }
            }
        }
    }

    private fun setUpHotRecyclerView() {
        binding.hotRecycler.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = hotAdapter
        }
    }

    private fun getHotMeal() {
        homeViewModel.getHotMeals()
        homeViewModel.getHotMealLiveData.observe(viewLifecycleOwner) { data ->
            hotAdapter.differ.submitList(data)
        }
    }

    private fun getCategories() {
        homeViewModel.getCategoriesHomeFragment()
        lifecycleScope.launch {
            homeViewModel.getCategoriesStateFlow.collect { data ->
                categoriesHomeAdapter.differ.submitList(data)
            }
        }
    }

    private fun setUpCategoriesRecyclerView() {
        binding.categoryRecycler.apply {
            layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
            adapter = categoriesHomeAdapter
        }
    }
}