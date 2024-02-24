package com.example.mealtoday.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.mealtoday.R
import com.example.mealtoday.adapters.DailyStateAdapter
import com.example.mealtoday.adapters.MoreAdapter
import com.example.mealtoday.adapters.SliderAdapter
import com.example.mealtoday.data.Slider
import com.example.mealtoday.databinding.FragmentMoreBinding
import com.example.mealtoday.utils.StickyItemDecoration
import com.example.mealtoday.viewModel.MoreViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.platform.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class MoreFragment : Fragment(R.layout.fragment_more) {

    private val moreViewModel: MoreViewModel by viewModels()

    private lateinit var binding: FragmentMoreBinding
    private lateinit var moreAdapter: MoreAdapter
    private var sliderList: ArrayList<Slider> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        enterTransition = MaterialFadeThrough().addTarget(view)
        reenterTransition = MaterialFadeThrough().addTarget(view)
        super.onViewCreated(view, savedInstanceState)

        setUpMoreRecyclerView()
    }

    private fun setUpMoreRecyclerView() {
        moreViewModel.getSliderMeals()
        moreViewModel.getSliderMealLiveData.observe(viewLifecycleOwner) { data ->
            sliderList.clear()
            sliderList.addAll(data)
            moreAdapter = MoreAdapter(requireActivity(), sliderList)
            binding.rvMore.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = moreAdapter
                val itemDecoration = StickyItemDecoration(getSectionCallback())
                addItemDecoration(itemDecoration)
            }
        }
    }

    private fun getSectionCallback(): StickyItemDecoration.SectionCallback {
        return object : StickyItemDecoration.SectionCallback {
            override fun isHeader(position: Int): Boolean {
                return moreAdapter.isHeader(position)
            }

            override fun getHeaderLayoutView(list: RecyclerView, position: Int): View? {
                return moreAdapter.getHeaderView(list, position)
            }
        }
    }
}