package com.example.mealtoday.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mealtoday.R
import com.example.mealtoday.adapters.DailyStateAdapter
import com.example.mealtoday.adapters.SliderAdapter
import com.example.mealtoday.data.Slider
import com.example.mealtoday.databinding.FragmentMoreBinding
import com.example.mealtoday.viewModel.MoreViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.platform.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreFragment : Fragment(R.layout.fragment_more) {

    private val moreViewModel: MoreViewModel by viewModels()

    private lateinit var binding: FragmentMoreBinding
    private lateinit var sliderAdapter: SliderAdapter
    private var sliderList: ArrayList<Slider> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sliderAdapter = SliderAdapter()
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

        binding.scrollView.run {
            isHeader = binding.tabLayout
        }

        setUpSliderRecyclerView()
        setUpViewPager()
    }

    private fun setUpSliderRecyclerView() {
        moreViewModel.getSliderMeals()
        moreViewModel.getSliderMealLiveData.observe(viewLifecycleOwner) { data ->
            sliderList.clear()
            sliderList.addAll(data)
            sliderAdapter.differ.submitList(sliderList)

            with(binding.rvBanner) {
                layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
                adapter = sliderAdapter
                setHasFixedSize(true)
            }
        }
    }

    private fun setUpViewPager() {
        val daysOfWeek = arrayOf("월", "화", "수", "목", "금", "토", "일")

        with(binding.tabViewPager) {
            adapter = DailyStateAdapter(this@MoreFragment)
        }

        TabLayoutMediator(binding.tabLayout, binding.tabViewPager) { tab, position ->
            tab.text = daysOfWeek[position]
        }.attach()
    }
}