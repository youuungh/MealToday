package com.example.mealtoday.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mealtoday.ui.fragments.DailyMealFragment

class DailyFragmentAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        return DailyMealFragment.newInstance(position)
    }
}