package com.example.mealtoday.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mealtoday.R
import com.example.mealtoday.adapters.HotAdapter
import com.example.mealtoday.databinding.FragmentHomeBinding
import com.example.mealtoday.viewModel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var hotAdapter : HotAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hotAdapter = HotAdapter()

        getRandomMeal()
        getHotMeal()
        setUpHotRecyclerView()
    }

    private fun setUpHotRecyclerView() {
        binding.hotRecycler.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = hotAdapter
        }
    }

    private fun getRandomMeal() {
        homeViewModel.getRandomMeal()
        homeViewModel.getRandomMealLiveData.observe(viewLifecycleOwner) { data ->
            run {
                Glide.with(this)
                    .load(data.strMealThumb)
                    .into(binding.randomImage)
            }
        }
    }

    private fun getHotMeal() {
        homeViewModel.getHotMeals()
        homeViewModel.getHotMealLiveData.observe(viewLifecycleOwner) { data ->
            hotAdapter.differ.submitList(data)
        }
    }
}