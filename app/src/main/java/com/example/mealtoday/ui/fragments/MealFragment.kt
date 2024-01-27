package com.example.mealtoday.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mealtoday.R
import com.example.mealtoday.databinding.FragmentMealBinding
import com.example.mealtoday.ui.activities.MainActivity
import com.example.mealtoday.utils.fitSystemWindowsWithAdjustResize
import com.google.android.material.transition.platform.MaterialContainerTransform

class MealFragment : Fragment(R.layout.fragment_meal) {
    private lateinit var binding: FragmentMealBinding
    private lateinit var navController: NavController
    private val args: MealFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = 300L
            scrimColor = Color.TRANSPARENT
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMealBinding.bind(view)
        navController = Navigation.findNavController(view)

        val activity = activity as MainActivity

        binding.backButton.setOnClickListener {
            navController.popBackStack()
        }

        getMealInfo()

        activity.onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.popBackStack()
            }
        })
    }

    private fun getMealInfo() {

        binding.tvIns.text = args.mealTitle

        Glide.with(this)
            .load(args.mealThumb)
            .into(binding.mealImage)
    }
}