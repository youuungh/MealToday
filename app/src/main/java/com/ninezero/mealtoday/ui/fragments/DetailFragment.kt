package com.ninezero.mealtoday.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ninezero.mealtoday.DRINK
import com.ninezero.mealtoday.HOT_MEAL
import com.ninezero.mealtoday.R
import com.ninezero.mealtoday.adapters.DetailAdapter
import com.ninezero.mealtoday.adapters.DrinkAdapter
import com.ninezero.mealtoday.databinding.FragmentDetailBinding
import com.ninezero.mealtoday.ui.fragments.DetailFragmentDirections
import com.ninezero.mealtoday.viewModel.HomeViewModel
import com.ninezero.mealtoday.viewModel.MoreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val homeViewModel: HomeViewModel by viewModels()
    private val moreViewModel: MoreViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    private lateinit var navController: NavController
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

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
        reenterTransition = com.google.android.material.transition.platform.MaterialSharedAxis(
            com.google.android.material.transition.platform.MaterialSharedAxis.X,
            false
        ).addTarget(view)
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailBinding.bind(view)
        navController = Navigation.findNavController(view)

        when (args.type) {
            HOT_MEAL -> loadHotAll()
            DRINK -> loadDrinkAll()
        }

        binding.back.setOnClickListener { navController.popBackStack() }
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

        detailAdapter.onDetailItemClick = { data ->
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToDetailOverViewFragment(
                args.type.toString(), data.idMeal, data.strMealThumb, data.strMeal, "", ""
            ))
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

        drinkAdapter.onDrinkItemClick = { data ->
            findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToDetailOverViewFragment(
                args.type.toString(), data.idDrink, data.strDrinkThumb, data.strDrink, data.strCategory, data.strAlcoholic
            ))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}