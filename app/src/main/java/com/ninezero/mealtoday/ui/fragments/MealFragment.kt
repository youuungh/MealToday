package com.ninezero.mealtoday.ui.fragments

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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ninezero.mealtoday.R
import com.ninezero.mealtoday.model.Meal
import com.ninezero.mealtoday.databinding.FragmentMealBinding
import com.ninezero.mealtoday.ui.activities.MainActivity
import com.ninezero.mealtoday.viewModel.MealViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MealFragment : Fragment(R.layout.fragment_meal) {

    private val mealViewModel: MealViewModel by viewModels()
    private val args: MealFragmentArgs by navArgs()

    private lateinit var navController: NavController
    private lateinit var binding: FragmentMealBinding
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

        (activity as? MainActivity)?.apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.title = " "
        }
        binding.toolbar.setupWithNavController(navController)
        binding.collapsing.title = " "

        getMealInfo()
        observeMealInfoData()
        setAppBarOffset()
    }

    private fun getMealInfo() {
        binding.title.text = args.mealTitle

        Glide.with(this@MealFragment)
            .load(args.mealThumb)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.mealImage)
    }

    private fun observeMealInfoData() {
        mealViewModel.getMealInfo(args.mealId)
        mealViewModel.getMealInfoLiveData.observe(viewLifecycleOwner) { data ->
            saveMeal = data

            binding.apply {
                subtitle.isVisible = true
                ingredientContainer.isVisible = true

                category.text = data.strCategory
                area.text = data.strArea
                content.text = data.strInstructions

                mealViewModel.isFavorite(data.idMeal).observe(viewLifecycleOwner) {
                    isFavorite = it.isNotEmpty()
                    setFavoriteLogo(isFavorite)
                }

                favorite.setOnClickListener {
                    saveMeal?.let { meal ->
                        val msg = if (isFavorite) "Favorite에서 삭제되었습니다" else "Favorite에 추가되었습니다"
                        setSnackBar(msg)
                        if (isFavorite) mealViewModel.deleteMeal(meal) else mealViewModel.upsertMeal(meal)
                    }
                }

                video.setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(data.strYoutube)))
                }

                val ingredients = (1..20)
                    .mapNotNull { i ->
                        val ingredient = data.getIngredient(i)
                        val measure = data.getMeasure(i)
                        if (!ingredient.isNullOrBlank() && !measure.isNullOrBlank()) {
                            "$ingredient: $measure"
                        } else null
                    }
                    .joinToString("\n")
                ingredient.text = ingredients
            }
        }
    }

    private fun Meal.getIngredient(index: Int): String? {
        val fieldName = "strIngredient$index"
        return try {
            javaClass.getDeclaredField(fieldName).apply { isAccessible = true }.get(this) as? String
        } catch (e: Exception) {
            null
        }
    }

    private fun Meal.getMeasure(index: Int): String? {
        val fieldName = "strMeasure$index"
        return try {
            javaClass.getDeclaredField(fieldName).apply { isAccessible = true }.get(this) as? String
        } catch (e: Exception) {
            null
        }
    }

    private fun setFavoriteLogo(isFavorite: Boolean) {
        val drawableRes = if (isFavorite) R.drawable.ic_favorite_fill else R.drawable.ic_favorite
        binding.favorite.setImageDrawable(ContextCompat.getDrawable(binding.favorite.context, drawableRes))
    }

    private fun setSnackBar(result: String) {
        Snackbar.make(binding.root, result, Snackbar.LENGTH_SHORT).apply {
            animationMode = Snackbar.ANIMATION_MODE_FADE
        }.show()
    }

    private fun setAppBarOffset() {
        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val window = requireActivity().window
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