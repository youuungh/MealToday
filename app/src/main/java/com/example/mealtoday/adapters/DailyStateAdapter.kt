package com.example.mealtoday.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mealtoday.ui.fragments.DailyDrinkFragment

class DailyStateAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        return DailyDrinkFragment.newInstance(position)
    }
}