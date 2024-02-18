package com.example.mealtoday.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mealtoday.R
import com.example.mealtoday.databinding.FragmentDailyMealBinding

class DailyMealFragment : Fragment(R.layout.fragment_daily_meal) {

    private lateinit var binding: FragmentDailyMealBinding

    companion object {
        private const val ARG_DAY_INDEX = "dayIndex"

        fun newInstance(dayIndex: Int): DailyMealFragment {
            val fragment = DailyMealFragment()
            val args = Bundle()
            args.putInt(ARG_DAY_INDEX, dayIndex)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDailyMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dayIndex = arguments?.getInt(ARG_DAY_INDEX) ?: 0
    }
}