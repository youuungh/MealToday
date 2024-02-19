package com.example.mealtoday.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealtoday.data.Meal
import com.example.mealtoday.repository.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

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
            } catch (t:Throwable) {
                Log.d("TAG", t.message.toString() + "MealInfo 에러")
            }
        }
    }

    fun upsertMeal(meal: Meal) = viewModelScope.launch {
        mealRepository.upsertMeal(meal)
    }

    fun deleteMeal(meal: Meal) = viewModelScope.launch {
        mealRepository.deleteMeal(meal)
    }

    fun isFavorite(id: String?): LiveData<List<Meal>> {
        return mealRepository.getFavoriteMeal(id)
    }
    fun getAllFavoriteMeal() = mealRepository.getAllFavoriteMeal
}