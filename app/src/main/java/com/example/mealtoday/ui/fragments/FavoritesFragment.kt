package com.example.mealtoday.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mealtoday.R
import com.example.mealtoday.adapters.FavoriteAdapter
import com.example.mealtoday.databinding.FragmentFavoritesBinding
import com.example.mealtoday.ui.activities.MainActivity
import com.example.mealtoday.utils.SwipeToDelete
import com.example.mealtoday.viewModel.MealViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private val mealViewModel: MealViewModel by viewModels()

    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteAdapter = FavoriteAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        enterTransition = MaterialFadeThrough().addTarget(view)
        reenterTransition = MaterialFadeThrough().addTarget(view)
        super.onViewCreated(view, savedInstanceState)

        setUpFavorite()
        swipeToDelete(binding.rvFavorite)
    }

    private fun setUpFavorite() {
        lifecycleScope.launch {
            mealViewModel.getAllFavoriteMeal().collect { favoriteData ->
                favoriteAdapter.differ.submitList(favoriteData)
                if (favoriteData.isNotEmpty()) {
                    binding.noDataContainer.isVisible = false
                    binding.tvItemCounts.isVisible = true
                    binding.tvItemCounts.text = favoriteData.size.toString()
                } else {
                    binding.noDataContainer.isVisible = true
                    binding.tvItemCounts.isVisible = false
                }
            }
        }

        with(binding.rvFavorite) {
            layoutManager = LinearLayoutManager(context)
            adapter = favoriteAdapter
            setHasFixedSize(true)
        }
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val meal = favoriteAdapter.differ.currentList[position]
                mealViewModel.deleteMeal(meal)

                val snackBar = Snackbar.make(
                    binding.root, "항목 1개가 삭제됨", Snackbar.LENGTH_LONG
                ).addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                    }

                    override fun onShown(transientBottomBar: Snackbar?) {
                        transientBottomBar?.setAction("실행취소") {
                            mealViewModel.upsertMeal(meal)
                        }
                        super.onShown(transientBottomBar)
                    }
                }).apply {
                    anchorView = activity?.findViewById(R.id.bottomNavigation)
                    animationMode = Snackbar.ANIMATION_MODE_FADE
                }
                snackBar.show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}