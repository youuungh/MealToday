package com.ninezero.mealtoday.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninezero.mealtoday.model.Hot
import com.ninezero.mealtoday.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
): ViewModel() {

    private var _categoryStateFlow = MutableStateFlow<List<Hot>>(emptyList())
    var categoryStateFlow: StateFlow<List<Hot>> = _categoryStateFlow

    private var categoryCached: List<Hot>? = null

    fun getCategory(categoryName: String) {
        categoryCached?.let {
            _categoryStateFlow.value = it
            return
        }
        viewModelScope.launch {
            try {
                val response = categoryRepository.getCategory(categoryName)
                if (response.isSuccessful) {
                    _categoryStateFlow.emit(response.body()!!.meals)
                    categoryCached = response.body()!!.meals
                }
            } catch (t:Throwable) {
                Log.d("TAG", t.message.toString() + "Category 에러")
            }
        }
    }
}