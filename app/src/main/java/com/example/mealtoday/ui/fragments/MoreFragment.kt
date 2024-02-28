package com.example.mealtoday.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.mealtoday.R
import com.example.mealtoday.adapters.BannerAdapter
import com.example.mealtoday.adapters.CockTailAdapter
import com.example.mealtoday.data.Banner
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
    private var bannerList: ArrayList<Banner> = ArrayList()
    private var currentPagePosition = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bannerAdapter = BannerAdapter()
        cockTailAdapter = CockTailAdapter()
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
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }
        enterTransition = MaterialFadeThrough().addTarget(view)
        reenterTransition = MaterialFadeThrough().addTarget(view)
        super.onViewCreated(view, savedInstanceState)

        setUpBanner()
        setUpCocktail()
    }

    private fun setUpBanner() {
        moreViewModel.getBannerMeals()
        moreViewModel.getBannerMealLiveData.observe(viewLifecycleOwner) { data ->
            bannerList.clear()
            bannerList.addAll(data)
            bannerAdapter.differ.submitList(bannerList)

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
    }

    private fun setUpCocktail() {
        lifecycleScope.launch {
            moreViewModel.getDrinks("Cocktail")
            moreViewModel.drinkStateFlow.collect { data ->
                cockTailAdapter.differ.submitList(data)
            }
        }

        CarouselSnapHelper().attachToRecyclerView(binding.cocktail)
        with(binding.cocktail) {
            adapter = cockTailAdapter
        }
    }
}