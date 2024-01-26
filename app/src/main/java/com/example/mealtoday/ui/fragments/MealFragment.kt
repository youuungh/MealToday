package com.example.mealtoday.ui.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.mealtoday.R
import com.example.mealtoday.databinding.FragmentMealBinding
import com.example.mealtoday.ui.activities.MainActivity
import com.google.android.material.transition.platform.MaterialContainerTransform

class MealFragment : Fragment(R.layout.fragment_meal) {
    private lateinit var binding: FragmentMealBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration = 600L
        }
//        val animation = MaterialContainerTransform().apply {
//            drawingViewId = R.id.fragment
//            duration = 300L
//            scrimColor = Color.TRANSPARENT
//        }
//        val animation = TransitionInflater.from(requireContext()).inflateTransition(
//            android.R.transition.move
//        )
//        sharedElementEnterTransition = animation
//        sharedElementReturnTransition = animation
//        exitTransition = MaterialElevationScale(false).apply { duration = 250 }
//        enterTransition = MaterialElevationScale(true).apply { duration = 300 }
//        reenterTransition = MaterialElevationScale(true).apply { duration = 300 }
//        addSharedElementListener()
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
    }


}