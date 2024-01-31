package com.example.mealtoday.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mealtoday.R
import com.example.mealtoday.adapters.FavoriteAdapter
import com.example.mealtoday.databinding.FragmentFavoritesBinding
import com.example.mealtoday.utils.doOnApplyWindowInsets
import com.example.mealtoday.viewModel.MealViewModel
import com.google.android.material.transition.platform.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private val mealViewModel: MealViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteAdapter = FavoriteAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enterTransition = MaterialFadeThrough().addTarget(binding.contentContainer)
        reenterTransition = MaterialFadeThrough().addTarget(binding.contentContainer)

        setUpFavoriteRecyclerView()
        getSavedData()

        requireView().doOnApplyWindowInsets { insetView, windowInsets, initialPadding, _ ->
            insetView.updatePadding(
                top = initialPadding.top + windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
            )
        }
    }

    private fun getSavedData() {
        lifecycleScope.launch {
            mealViewModel.getSavedMeal().collect { savedData ->
                favoriteAdapter.differ.submitList(savedData)
                if (savedData.isNotEmpty()) {
                    binding.noDataContainer.isVisible = false
                    binding.tvItemCounts.isVisible = true
                    binding.tvItemCounts.text = savedData.size.toString()
                } else {
                    binding.noDataContainer.isVisible = true
                    binding.tvItemCounts.isVisible = false
                }
            }
        }
    }

    private fun setUpFavoriteRecyclerView() {
        binding.rvFavorite.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteAdapter
            setHasFixedSize(true)
        }
    }
}