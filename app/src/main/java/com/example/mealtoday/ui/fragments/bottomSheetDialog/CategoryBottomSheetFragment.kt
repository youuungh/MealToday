package com.example.mealtoday.ui.fragments.bottomSheetDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mealtoday.R
import com.example.mealtoday.databinding.FragmentCategoryBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryBottomSheetFragment : Fragment(R.layout.fragment_category_bottom_sheet) {

    private lateinit var binding: FragmentCategoryBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }
}