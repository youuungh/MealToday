package com.example.mealtoday.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealtoday.DRINK
import com.example.mealtoday.HOT_MEAL
import com.example.mealtoday.R
import com.example.mealtoday.adapters.DetailAdapter
import com.example.mealtoday.adapters.DrinkAdapter
import com.example.mealtoday.adapters.HotAdapter
import com.example.mealtoday.databinding.FragmentDetailBinding
import com.example.mealtoday.viewModel.HomeViewModel
import com.example.mealtoday.viewModel.MoreViewModel
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val homeViewModel: HomeViewModel by viewModels()
    private val moreViewModel: MoreViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        enterTransition = com.google.android.material.transition.platform.MaterialSharedAxis(
            com.google.android.material.transition.platform.MaterialSharedAxis.Z,
            true
        ).addTarget(view)
        returnTransition = com.google.android.material.transition.platform.MaterialSharedAxis(
            com.google.android.material.transition.platform.MaterialSharedAxis.Z,
            false
        ).addTarget(view)
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailBinding.bind(view)
        navController = Navigation.findNavController(view)

        when (args.type) {
            HOT_MEAL -> loadHotAll()
            DRINK -> loadDrinkAll()
        }

        binding.appBarLayout.statusBarForeground =
            MaterialShapeDrawable.createWithElevationOverlay(requireContext())

        binding.back.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun loadHotAll() {
        binding.detailTitle.text = "인기있는"

        val detailAdapter = DetailAdapter(args.type)

        homeViewModel.getHotMeals()
        homeViewModel.getHotMealLiveData.observe(viewLifecycleOwner) { data ->
            detailAdapter.differ.submitList(data)
        }

        with(binding.detailRecycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = detailAdapter
        }
    }

    private fun loadDrinkAll() {
        binding.detailTitle.text = "추천 음료"

        val drinkAdapter = DrinkAdapter()

        moreViewModel.getDrinks()
        moreViewModel.getDrinkMealLiveData.observe(viewLifecycleOwner) { data ->
            drinkAdapter.differ.submitList(data)
        }

        with(binding.detailRecycler) {
            layoutManager = LinearLayoutManager(context)
            adapter = drinkAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}