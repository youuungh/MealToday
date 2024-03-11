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
import kotlinx.coroutines.Dispatchers.IO
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

    private var bannerMealsCached: ArrayList<Banner>? = null

    fun getBannerMeals() {
        bannerMealsCached?.let {
            _getBannerMealLiveData.postValue(bannerMealsCached!!)
            return
        }
        viewModelScope.launch(IO) {
            try {
                val response = moreRepository.getBannerMeal("Canadian")
                if (response.isSuccessful) {
                    response.body()?.meals?.let {
                        _getBannerMealLiveData.postValue(it)
                        bannerMealsCached = it
                    }
                }
            } catch (t:Throwable) {
                Log.d("TAG", t.message.toString() + "BannerMeal 에러")
            }
        }
    }

    private val _cocktailStateFlow = MutableStateFlow<List<Cocktail>>(emptyList())
    val cocktailStateFlow: StateFlow<List<Cocktail>> = _cocktailStateFlow

    private var cocktailCached: List<Cocktail>? = null

    fun getCocktails(cocktailName: String) {
        cocktailCached?.let {
            _cocktailStateFlow.value = it
            return
        }
        viewModelScope.launch(IO) {
            try {
                val response = moreRepository.getCocktails(cocktailName)
                if (response.isSuccessful) {
                    response.body()?.drinks?.let {
                        _cocktailStateFlow.emit(it)
                        cocktailCached = it
                    }
                }
            } catch (t:Throwable) {
                Log.d("TAG", t.message.toString() + "CockTail 에러")
            }
        }
    }

    private val _getDrinkMealLiveData = MutableLiveData<List<Drink>>()
    val getDrinkMealLiveData: LiveData<List<Drink>> = _getDrinkMealLiveData

    private var drinkCached: List<Drink>? = null

    fun getDrinks() {
        drinkCached?.let {
            _getDrinkMealLiveData.postValue(it)
            return
        }
        viewModelScope.launch(IO) {
            try {
                val response = moreRepository.getDrinks("a")
                if (response.isSuccessful) {
                    response.body()?.drinks?.let {
                        _getDrinkMealLiveData.postValue(it)
                        drinkCached = it
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
        viewModelScope.launch(IO) {
            try {
                val response = moreRepository.getBeverageInfo(beverageId)
                if (response.isSuccessful) {
                    _getBeverageInfoLiveData.postValue(response.body()?.drinks?.getOrNull(0))
                }
            } catch (t:Throwable) {
                Log.d("TAG", t.message.toString() + "BeverageInfo 에러")
            }
        }
    }
}