package com.example.mealtoday.ui.fragments.bottomSheetDialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mealtoday.R
import com.example.mealtoday.databinding.DrinkBottomSheetBinding
import com.example.mealtoday.model.Beverage
import com.example.mealtoday.model.Drink
import com.example.mealtoday.model.Hot
import com.example.mealtoday.viewModel.MealViewModel
import com.example.mealtoday.viewModel.MoreViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DrinkBottomSheet : BottomSheetDialogFragment() {

    private val moreViewModel: MoreViewModel by viewModels()
    private lateinit var args: Drink

    private var _binding: DrinkBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DrinkBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDrinkInfo()
        observeDrinkInfoData()
    }

    private fun getDrinkInfo() {
        binding.apply {
            title.isSelected = true
            title.text = args.strDrink
            category.text = args.strCategory
            alcoholic.text = args.strAlcoholic
            glass.text = args.strGlass
        }

        Glide.with(requireContext())
            .load(args.strDrinkThumb)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.contentImage)
    }

    private fun observeDrinkInfoData() {
        moreViewModel.getBeverageInfo(args.idDrink)
        moreViewModel.getBeverageInfoLiveData.observe(viewLifecycleOwner) { data ->
            binding.content.text = data.strInstructions
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setData(data: Drink) {
        args = data
    }
}