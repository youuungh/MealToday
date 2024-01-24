package com.example.mealtoday.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealtoday.db.Hot
import com.example.mealtoday.db.Meal
import com.example.mealtoday.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _getRandomMealLiveData = MutableLiveData<Meal>()
    val getRandomMealLiveData : LiveData<Meal> = _getRandomMealLiveData

    fun getRandomMeal() {
        viewModelScope.launch {
            val response = homeRepository.getRandomMeal()
            response.body()!!.meals.let {
                _getRandomMealLiveData.postValue(it[0])
            }
        }
    }

    private val _getHotMealLiveData = MutableLiveData<List<Hot>>()
    val getHotMealLiveData: LiveData<List<Hot>> = _getHotMealLiveData

    fun getHotMeals() {
        viewModelScope.launch {
            val response = homeRepository.getHotMeals("Seafood")
            if (response.isSuccessful) {
                response.body()?.meals?.let {
                    _getHotMealLiveData.postValue(it)
                }
            }
        }
    }
}