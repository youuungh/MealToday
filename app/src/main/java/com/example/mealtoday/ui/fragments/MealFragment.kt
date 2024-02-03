package com.example.mealtoday.ui.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.mealtoday.R
import com.example.mealtoday.data.Meal
import com.example.mealtoday.databinding.FragmentMealBinding
import com.example.mealtoday.ui.activities.MainActivity
import com.example.mealtoday.viewModel.MealViewModel
import com.google.android.material.transition.platform.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MealFragment : Fragment(R.layout.fragment_meal) {

    private val mealViewModel: MealViewModel by viewModels()
    private val args: MealFragmentArgs by navArgs()

    private lateinit var binding: FragmentMealBinding
    private lateinit var navController: NavController
    private var saveMeal: Meal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.hostFragment
            duration = 300L
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        ViewCompat.setTransitionName(binding.contentContainer, "trans_${args.mealId}")

        val activity = activity as MainActivity
        activity.setSupportActionBar(binding.toolbar)

        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.title = " "
        binding.collapsing.title = " "

        setAppBarOffset(activity)
        getMealInfo()
        observeGetMealInfoData()
    }

    private fun getMealInfo() {
        binding.tvTitle.text = args.mealTitle

        Glide.with(this)
            .load(args.mealThumb)
            .override(300, 300)
            .into(binding.mealImage)
    }
    private fun observeGetMealInfoData() {
        mealViewModel.getMealInfo(args.mealId)
        mealViewModel.getMealInfoLiveData.observe(viewLifecycleOwner) { data ->
            saveMeal = data
            binding.category.text = data.strCategory
            binding.location.text = data.strArea
            binding.tvContent.text = data.strInstructions
            binding.videoButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data.strYoutube))
                startActivity(intent)
            }
            binding.favoriteButton.setOnClickListener {
                saveMeal?.let { meal ->
                    mealViewModel.upsertMeal(meal)
                }
            }
        }
    }

    private fun setAppBarOffset(activity: Activity) {
        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val window = activity.window
            val collapsingHeight = binding.collapsing.height
            val minimumHeight = ViewCompat.getMinimumHeight(binding.collapsing) * 2
            val controller = WindowCompat.getInsetsController(window, window.decorView)

            val colorFilter = if ((collapsingHeight + verticalOffset) < minimumHeight) {
                controller.isAppearanceLightStatusBars = true
                PorterDuffColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.black),
                    PorterDuff.Mode.SRC_ATOP
                )
            } else {
                controller.isAppearanceLightStatusBars = false
                PorterDuffColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.white),
                    PorterDuff.Mode.SRC_ATOP
                )
            }

            binding.toolbar.navigationIcon?.colorFilter = colorFilter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.let {
            WindowCompat.getInsetsController(
                it.window,
                requireActivity().window.decorView
            ).isAppearanceLightStatusBars = true
        }
    }
}