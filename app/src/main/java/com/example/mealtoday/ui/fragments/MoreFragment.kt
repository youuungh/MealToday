package com.example.mealtoday.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mealtoday.DRINK
import com.example.mealtoday.R
import com.example.mealtoday.adapters.BannerAdapter
import com.example.mealtoday.adapters.CockTailAdapter
import com.example.mealtoday.adapters.DrinkAdapter
import com.example.mealtoday.model.Banner
import com.example.mealtoday.databinding.FragmentMoreBinding
import com.example.mealtoday.ui.fragments.bottomSheetDialog.DrinkBottomSheet
import com.example.mealtoday.viewModel.MoreViewModel
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.transition.platform.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoreFragment : Fragment(R.layout.fragment_more) {

    private val moreViewModel: MoreViewModel by viewModels()

    private lateinit var binding: FragmentMoreBinding
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var cocktailAdapter: CockTailAdapter
    private lateinit var drinkAdapter: DrinkAdapter

    private var bannerList: ArrayList<Banner> = ArrayList()
    private var currentPagePosition = 0
    private val handler = Handler(Looper.myLooper()!!)
    private val delayMillis = 2000L

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

        bannerAdapter = BannerAdapter()
        cocktailAdapter = CockTailAdapter()
        drinkAdapter = DrinkAdapter()

        setUpBanner()
        setUpCocktail()
        setUpDrink()
        onAboutClick()
    }

    private fun setUpBanner() {
        moreViewModel.getBannerMeals()
        moreViewModel.getBannerMealLiveData.observe(viewLifecycleOwner) { data ->
            bannerList.clear()
            bannerList.addAll(data)
            bannerAdapter.differ.submitList(bannerList)

            binding.max.text = bannerList.size.toString()
        }

        with(binding.banner) {
            adapter = bannerAdapter
            setCurrentItem(currentPagePosition, false)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.current.text ="${position + 1}"
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    when (state) {
                        ViewPager2.SCROLL_STATE_IDLE -> handlerStart(delayMillis)
                        ViewPager2.SCROLL_STATE_DRAGGING -> handlerStop()
                    }
                }
            })
        }
    }

    private fun setUpCocktail() {
        lifecycleScope.launch {
            moreViewModel.getCocktails("Cocktail")
            moreViewModel.cocktailStateFlow.collect { data ->
                cocktailAdapter.differ.submitList(data)
            }
        }

        with(binding.cocktailRecycler) {
            adapter = cocktailAdapter
            CarouselSnapHelper().attachToRecyclerView(this)
        }
    }

    private fun setUpDrink() {
        moreViewModel.getDrinks()
        moreViewModel.getDrinkMealLiveData.observe(viewLifecycleOwner) { data ->
            drinkAdapter.differ.submitList(data)
        }

        with(binding.drinkRecycler) {
            layoutManager = GridLayoutManager(context, 5, RecyclerView.HORIZONTAL, false)
            adapter = drinkAdapter
            PagerSnapHelper().attachToRecyclerView(this)
        }

        drinkAdapter.onDrinkItemClick = { data ->
            DrinkBottomSheet().apply {
                setData(data)
            }.show(childFragmentManager, "DrinkBottomSheet")
        }

        binding.drinkAll.setOnClickListener {
            findNavController().navigate(MoreFragmentDirections.actionMoreFragmentToDetailFragment(
                DRINK
            ))
        }
    }

    private fun onAboutClick() {
        binding.about.setOnClickListener {
            findNavController().navigate(MoreFragmentDirections.actionMoreFragmentToAboutFragment())
        }
    }

    private fun handlerStart(delayMillis: Long) {
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            currentPagePosition = (currentPagePosition + 1) % bannerList.size
            binding.banner.setCurrentItem(currentPagePosition, true)
        }, delayMillis)
    }


    private fun handlerStop(){
        handler.removeCallbacksAndMessages(null)
    }

    override fun onPause() {
        super.onPause()
        handlerStop()
    }

    override fun onResume() {
        super.onResume()
        handlerStart(delayMillis)
    }
}