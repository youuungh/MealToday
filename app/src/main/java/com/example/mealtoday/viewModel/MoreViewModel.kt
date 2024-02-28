package com.example.mealtoday.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealtoday.data.Drink
import com.example.mealtoday.data.Banner
import com.example.mealtoday.repository.MoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val moreRepository: MoreRepository
): ViewModel() {

    private val _getBannerMealLiveData = MutableLiveData<ArrayList<Banner>>()
    val getBannerMealLiveData: LiveData<ArrayList<Banner>> = _getBannerMealLiveData

    fun getBannerMeals() {
        viewModelScope.launch {
            try {
                val response = moreRepository.getBannerMeal("Canadian")
                if (response.isSuccessful) {
                    response.body()?.meals.let {
                        _getBannerMealLiveData.postValue(it)
                    }
                }
            } catch (t:Throwable) {
                Log.d("TAG", t.message.toString() + "BannerMeal 에러")
            }
        }
    }

    private val _drinkStateFlow = MutableStateFlow<List<Drink>>(emptyList())
    val  drinkStateFlow: StateFlow<List<Drink>> = _drinkStateFlow

    fun getDrinks(drinkName: String) {
        viewModelScope.launch {
            try {
                val response = moreRepository.getDrinks(drinkName)
                if (response.isSuccessful) {
                    _drinkStateFlow.emit(response.body()!!.drinks)
                }
            } catch (t:Throwable) {
                Log.d("TAG", t.message.toString() + "CockTail 에러")
            }
        }
    }
}