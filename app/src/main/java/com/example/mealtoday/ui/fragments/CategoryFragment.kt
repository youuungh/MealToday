package com.example.mealtoday.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.mealtoday.R
import com.example.mealtoday.databinding.FragmentCategoryBinding
import com.example.mealtoday.ui.activities.MainActivity
import com.example.mealtoday.utils.doOnApplyWindowInsets

class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var navController: NavController
    private val args: CategoryFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        requireView().doOnApplyWindowInsets { insetView, windowInsets, initialPadding, _ ->
            insetView.updatePadding(
                top = initialPadding.top + windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
            )
        }
    }
}