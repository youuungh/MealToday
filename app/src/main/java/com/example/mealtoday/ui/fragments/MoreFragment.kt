package com.example.mealtoday.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.mealtoday.DRINK
import com.example.mealtoday.HOT_MEAL
import com.example.mealtoday.R
import com.example.mealtoday.adapters.BannerAdapter
import com.example.mealtoday.adapters.BeverageAdapter
import com.example.mealtoday.adapters.CockTailAdapter
import com.example.mealtoday.adapters.DrinkAdapter
import com.example.mealtoday.model.Banner
import com.example.mealtoday.databinding.FragmentMoreBinding
import com.example.mealtoday.viewModel.MoreViewModel
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.transition.platform.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class MoreFragment : Fragment(R.layout.fragment_more) {

    private val moreViewModel: MoreViewModel by viewModels()

    private lateinit var binding: FragmentMoreBinding
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var cockTailAdapter: CockTailAdapter
    private lateinit var drinkAdapter: DrinkAdapter
    private lateinit var beverageAdapter: BeverageAdapter
    private var bannerList: ArrayList<Banner> = ArrayList()
    private var currentPagePosition = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bannerAdapter = BannerAdapter()
        cockTailAdapter = CockTailAdapter()
        drinkAdapter = DrinkAdapter()
        beverageAdapter = BeverageAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        enterTransition = MaterialFadeThrough().addTarget(view)
        reenterTransition = MaterialFadeThrough().addTarget(view)
        super.onViewCreated(view, savedInstanceState)

        setUpBanner()
        setUpCocktail()
        setUpDrink()
        onDrinkAllClick(view)
    }

    private fun setUpBanner() {
        moreViewModel.getBannerMeals()
        moreViewModel.getBannerMealLiveData.observe(viewLifecycleOwner) { data ->
            bannerList.clear()
            bannerList.addAll(data)
            bannerAdapter.differ.submitList(bannerList)
        }

        with(binding.banner) {
            adapter = bannerAdapter
            offscreenPageLimit = 3
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            setCurrentItem(currentPagePosition, false)
        }

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(40))

        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }

        binding.banner.setPageTransformer(compositePageTransformer)
    }

    private fun setUpCocktail() {
        lifecycleScope.launch {
            moreViewModel.getCocktails("Cocktail")
            moreViewModel.cocktailStateFlow.collect { data ->
                cockTailAdapter.differ.submitList(data)
            }
        }

        CarouselSnapHelper().attachToRecyclerView(binding.cocktailRecycler)
        with(binding.cocktailRecycler) {
            adapter = cockTailAdapter
        }
    }

    private fun setUpDrink() {
        moreViewModel.getDrinks()
        moreViewModel.getDrinkMealLiveData.observe(viewLifecycleOwner) { data ->
            drinkAdapter.differ.submitList(data)
        }

        PagerSnapHelper().attachToRecyclerView(binding.drinkRecycler)
        with(binding.drinkRecycler) {
            layoutManager = GridLayoutManager(context, 5, RecyclerView.HORIZONTAL, false)
            adapter = drinkAdapter
        }
    }

    private fun onDrinkAllClick(view: View) {
        binding.drinkAll.setOnClickListener {
            findNavController().navigate(MoreFragmentDirections.actionMoreFragmentToDetailFragment(
                DRINK
            ))
        }
    }
}