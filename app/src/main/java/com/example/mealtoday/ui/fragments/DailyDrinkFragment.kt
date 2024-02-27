package com.example.mealtoday.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mealtoday.R
import com.example.mealtoday.adapters.DrinkAdapter
import com.example.mealtoday.databinding.FragmentDailyDrinkBinding
import com.example.mealtoday.viewModel.MoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DailyDrinkFragment : Fragment(R.layout.fragment_daily_drink) {

    private val moreViewModel: MoreViewModel by viewModels()

    private lateinit var binding: FragmentDailyDrinkBinding
    private lateinit var drinkAdapter: DrinkAdapter

    companion object {
        private const val ARG_DAY_INDEX = "dayIndex"

        fun newInstance(dayIndex: Int): DailyDrinkFragment {
            val fragment = DailyDrinkFragment()
            val args = Bundle()
            args.putInt(ARG_DAY_INDEX, dayIndex)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drinkAdapter = DrinkAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDailyDrinkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDrinkInfo()
        setUpDrinkRecyclerView()
    }

    private fun getDrinkInfo() {
        val dayIndex = arguments?.getInt(ARG_DAY_INDEX) ?: 0
        val cocktailList = arrayOf("Punch / Party Drink", "Cocktail", "Shake", "Homemade Liqueur", "Shot", "Beer", "Soft Drink")
        val cocktail = if (dayIndex in cocktailList.indices) cocktailList[dayIndex] else cocktailList.last()

        lifecycleScope.launch {
            moreViewModel.getDrinks(cocktail)
            moreViewModel.drinkStateFlow.collect { data ->
                drinkAdapter.differ.submitList(data)
            }
        }
    }

    private fun setUpDrinkRecyclerView() {
        with(binding.rvDaily) {
            layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
            adapter = drinkAdapter
        }
    }
}