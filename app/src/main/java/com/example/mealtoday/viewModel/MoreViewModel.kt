package com.example.mealtoday.viewModel

import android.icu.text.Transliterator.Position
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealtoday.data.Meal
import com.example.mealtoday.data.Slider
import com.example.mealtoday.repository.MoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val moreRepository: MoreRepository
): ViewModel() {

    private val _getSliderMealLiveData = MutableLiveData<ArrayList<Slider>>()
    val getSliderMealLiveData: LiveData<ArrayList<Slider>> = _getSliderMealLiveData

    fun getSliderMeals() {
        viewModelScope.launch {
            try {
                val response = moreRepository.getSliderMeal("Canadian")
                if (response.isSuccessful) {
                    response.body()?.meals.let {
                        _getSliderMealLiveData.postValue(it)
                    }
                }
            } catch (t:Throwable) {
                Log.d("TAG", t.message.toString() + "SliderMeal 에러")
            }
        }
    }
}