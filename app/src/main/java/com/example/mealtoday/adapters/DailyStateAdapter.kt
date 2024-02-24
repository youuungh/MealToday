package com.example.mealtoday.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mealtoday.ui.fragments.DailyDrinkFragment

class DailyStateAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        return DailyDrinkFragment.newInstance(position)
    }
}