package com.example.mealtoday.ui.fragments.bottomSheetDialog

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updateMargins
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mealtoday.R
import com.example.mealtoday.databinding.CategoryBottomSheetBinding
import com.example.mealtoday.model.Meal
import com.example.mealtoday.utils.doOnApplyWindowInsets
import com.example.mealtoday.viewModel.MealViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryBottomSheet : BottomSheetDialogFragment() {

    private val mealViewModel: MealViewModel by viewModels()
    private val args: CategoryBottomSheetArgs by navArgs()

    private var _binding: CategoryBottomSheetBinding? = null
    private val binding get() = _binding!!
    private var saveMeal: Meal? = null
    private var isFavorite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CategoryBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMealInfo()
        observeMealInfoData()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        object : BottomSheetDialog(requireContext(), theme) {
            override fun onAttachedToWindow() {
                super.onAttachedToWindow()

                //

            }
        }

    private fun getMealInfo() {
        binding.title.text = args.mealTitle

        Glide.with(this@CategoryBottomSheet)
            .load(args.mealThumb)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.contentImage)
    }

    private fun observeMealInfoData() {
        mealViewModel.getMealInfo(args.mealId)
        mealViewModel.getMealInfoLiveData.observe(viewLifecycleOwner) { data ->
            saveMeal = data
            binding.apply {
                chipCategory.text = data.strCategory
                chipLocation.text = data.strArea
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}