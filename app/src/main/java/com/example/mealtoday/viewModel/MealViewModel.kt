package com.example.mealtoday.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealtoday.db.Meal
import com.example.mealtoday.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealViewModel @Inject constructor(
    private val mealRepository: MealRepository
): ViewModel() {

    private val _getMealInfoLiveData = MutableLiveData<Meal>()
    val getMealInfoLiveData: LiveData<Meal> = _getMealInfoLiveData

    fun getMealInfo(mealId: String) {
        viewModelScope.launch {
            try {
                val response = mealRepository.getMealInfo(mealId)
                if (response.isSuccessful) {
                    _getMealInfoLiveData.value = response.body()!!.meals[0]
                }
            } catch (t: Throwable) {
                Log.d("TAG", t.message.toString() + "MealInfo 에러")
            }
        }
    }
}