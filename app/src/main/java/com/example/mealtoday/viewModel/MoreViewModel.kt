package com.example.mealtoday.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealtoday.model.Banner
import com.example.mealtoday.model.Beverage
import com.example.mealtoday.model.Cocktail
import com.example.mealtoday.model.Drink
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

    private val _cocktailStateFlow = MutableStateFlow<List<Cocktail>>(emptyList())
    val cocktailStateFlow: StateFlow<List<Cocktail>> = _cocktailStateFlow

    fun getCocktails(cocktailName: String) {
        viewModelScope.launch {
            try {
                val response = moreRepository.getCocktails(cocktailName)
                if (response.isSuccessful) {
                    _cocktailStateFlow.emit(response.body()!!.drinks)
                }
            } catch (t:Throwable) {
                Log.d("TAG", t.message.toString() + "CockTail 에러")
            }
        }
    }

    private val _getDrinkMealLiveData = MutableLiveData<List<Drink>>()
    val getDrinkMealLiveData: LiveData<List<Drink>> = _getDrinkMealLiveData

    fun getDrinks() {
        viewModelScope.launch {
            try {
                val response = moreRepository.getDrinks("a")
                if (response.isSuccessful) {
                    response.body()?.drinks?.let {
                        _getDrinkMealLiveData.postValue(it)
                    }
                }
            } catch (t:Throwable) {
                Log.d("TAG", t.message.toString() + "Drink 에러")
            }
        }
    }

    private val _getBeverageInfoLiveData = MutableLiveData<Beverage>()
    val getBeverageInfoLiveData: LiveData<Beverage> = _getBeverageInfoLiveData

    fun getBeverageInfo(beverageId: String) {
        viewModelScope.launch {
            try {
                val response = moreRepository.getBeverageInfo(beverageId)
                if (response.isSuccessful) {
                    _getBeverageInfoLiveData.value = response.body()!!.drinks[0]
                }
            } catch (t:Throwable) {
                Log.d("TAG", t.message.toString() + "BeverageInfo 에러")
            }
        }
    }

}