package com.example.mealtoday.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mealtoday.R
import com.example.mealtoday.databinding.FragmentMealBinding

class MealFragment : Fragment() {
    private lateinit var binding: FragmentMealBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealBinding.inflate(inflater, container, false)
        return binding.root
    }
}