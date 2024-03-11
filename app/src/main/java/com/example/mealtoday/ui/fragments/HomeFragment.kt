package com.example.mealtoday.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnPreDraw
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
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mealtoday.HOT_MEAL
import com.example.mealtoday.R
import com.example.mealtoday.adapters.CategoriesHomeAdapter
import com.example.mealtoday.adapters.HotAdapter
import com.example.mealtoday.databinding.FragmentHomeBinding
import com.example.mealtoday.utils.doOnApplyWindowInsets
import com.example.mealtoday.viewModel.HomeViewModel
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.google.android.material.transition.platform.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding
    private lateinit var hotAdapter: HotAdapter
    private lateinit var categoriesHomeAdapter: CategoriesHomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        //enterTransition = MaterialFadeThrough().addTarget(view)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).addTarget(view)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).addTarget(view)
        super.onViewCreated(view, savedInstanceState)

        hotAdapter = HotAdapter()
        categoriesHomeAdapter = CategoriesHomeAdapter()

        setUpRandomMeal()
        setUpHotMeal()
        setUpCategories()
        onSearchClick()

        requireView().doOnApplyWindowInsets { insetView, windowInsets, initialPadding, _ ->
            insetView.updatePadding(
                top = initialPadding.top + windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
            )
        }
    }

    private fun setUpRandomMeal() {
        homeViewModel.getRandomMeal()
        homeViewModel.getRandomMealLiveData.observe(viewLifecycleOwner) { data ->
            binding.randomImageLayout.transitionName = "trans_${data.idMeal}"
            run {
                Glide.with(requireContext())
                    .load(data.strMealThumb)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.randomImage)

                try {
                    binding.randomImageLayout.setOnClickListener {
                        val extras = FragmentNavigatorExtras(binding.randomImageLayout to "trans_${data.idMeal}")
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

    private fun setUpHotMeal() {
        homeViewModel.getHotMeals()
        homeViewModel.getHotMealLiveData.observe(viewLifecycleOwner) { data ->
            hotAdapter.differ.submitList(data)
        }

        with(binding.hotRecycler) {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = hotAdapter
            setHasFixedSize(true)
        }

        binding.hotAll.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                HOT_MEAL
            ))
        }
    }

    private fun setUpCategories() {
        homeViewModel.getCategoriesHomeFragment()
        lifecycleScope.launch {
            homeViewModel.getCategoriesStateFlow.collect { data ->
                categoriesHomeAdapter.differ.submitList(data)
            }
        }

        with(binding.categoryRecycler) {
            layoutManager = GridLayoutManager(context, 4, RecyclerView.VERTICAL, false)
            adapter = categoriesHomeAdapter
        }

        categoriesHomeAdapter.onCategoryItemClick = { data ->
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                data.strCategory
            ))
        }
    }

    private fun onSearchClick() {
        binding.search.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
        }
    }
}