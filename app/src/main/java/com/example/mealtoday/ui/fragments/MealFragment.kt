package com.example.mealtoday.ui.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.mealtoday.R
import com.example.mealtoday.model.Meal
import com.example.mealtoday.databinding.FragmentMealBinding
import com.example.mealtoday.ui.activities.MainActivity
import com.example.mealtoday.viewModel.MealViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MealFragment : Fragment(R.layout.fragment_meal) {

    private val mealViewModel: MealViewModel by viewModels()
    private val args: MealFragmentArgs by navArgs()

    private lateinit var binding: FragmentMealBinding
    private lateinit var navController: NavController
    private var saveMeal: Meal? = null
    private var isFavorite: Boolean = false

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

        ViewCompat.setTransitionName(view, "trans_${args.mealId}")

        val activity = activity as MainActivity
        activity.setSupportActionBar(binding.toolbar)

        binding.toolbar.setupWithNavController(navController)
        binding.toolbar.title = " "
        binding.collapsing.title = " "

        setAppBarOffset(activity)
        getMealInfo()
        observeMealInfoData()
    }

    private fun getMealInfo() {
        binding.tvTitle.text = args.mealTitle

        val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

        Glide.with(this@MealFragment)
            .load(args.mealThumb)
            .apply(requestOptions)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.mealImage)
    }

    private fun observeMealInfoData() {
        mealViewModel.getMealInfo(args.mealId)
        mealViewModel.getMealInfoLiveData.observe(viewLifecycleOwner) { data ->
            saveMeal = data
            binding.category.text = data.strCategory
            binding.location.text = data.strArea
            binding.tvContent.text = data.strInstructions
            binding.videoButton.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(data.strYoutube)))
            }
            mealViewModel.isFavorite(data.idMeal).observe(viewLifecycleOwner) {
                isFavorite = if (it.isNotEmpty()) {
                    setFavoriteLogo(true)
                    true
                } else {
                    setFavoriteLogo(false)
                    false
                }
            }
            binding.favoriteButton.setOnClickListener {
                saveMeal?.let { meal ->
                    if (isFavorite) {
                        setSnackBar("Favorite에서 삭제되었습니다")
                        mealViewModel.deleteMeal(meal)
                    } else {
                        setSnackBar("Favorite에 추가되었습니다")
                        mealViewModel.upsertMeal(meal)
                    }
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

    private fun setFavoriteLogo(isFavorite: Boolean) {
        if(isFavorite) {
            binding.favoriteButton.setImageDrawable(ContextCompat
                .getDrawable(binding.favoriteButton.context, R.drawable.ic_favorite_fill))
        } else {
            binding.favoriteButton.setImageDrawable(ContextCompat
                .getDrawable(binding.favoriteButton.context, R.drawable.ic_favorite))
        }
    }

    private fun setSnackBar(result: String) {
        Snackbar.make(binding.root, result, Snackbar.LENGTH_SHORT).apply {
            animationMode = Snackbar.ANIMATION_MODE_FADE
        }.show()
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