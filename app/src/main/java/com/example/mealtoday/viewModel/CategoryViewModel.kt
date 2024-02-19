package com.example.mealtoday.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealtoday.data.Hot
import com.example.mealtoday.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
): ViewModel() {

    private var _categoryStateFlow = MutableStateFlow<List<Hot>>(emptyList())
    var categoryStateFlow: StateFlow<List<Hot>> = _categoryStateFlow

    fun getCategory(categoryName: String) {
        viewModelScope.launch {
            try {
                val response = categoryRepository.getCategory(categoryName)
                if (response.isSuccessful) {
                    _categoryStateFlow.emit(response.body()!!.meals)
                }
            } catch (t:Throwable) {
                Log.d("TAG", t.message.toString() + "Category 에러")
            }
        }
    }
}