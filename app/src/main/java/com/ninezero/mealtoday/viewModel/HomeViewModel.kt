package com.ninezero.mealtoday.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninezero.mealtoday.model.Category
import com.ninezero.mealtoday.model.Hot
import com.ninezero.mealtoday.model.Meal
import com.ninezero.mealtoday.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _getRandomMealLiveData = MutableLiveData<Meal>()
    val getRandomMealLiveData : LiveData<Meal> = _getRandomMealLiveData

    private var saveStateRandomMeal: Meal? = null

    fun getRandomMeal() {
        saveStateRandomMeal?.let { randomMeal ->
            _getRandomMealLiveData.postValue(randomMeal)
            return
        }
        viewModelScope.launch(IO) {
            try {
                val response = homeRepository.getRandomMeal()
                val meals = response.body()?.meals
                meals?.firstOrNull()?.let {
                    _getRandomMealLiveData.postValue(it)
                    saveStateRandomMeal = it
                }
            } catch (t:Throwable) {
                Log.d("TAG", t.message.toString() + "RandomMeal 에러")
            }
        }
    }

    private val _getHotMealLiveData = MutableLiveData<List<Hot>>()
    val getHotMealLiveData: LiveData<List<Hot>> = _getHotMealLiveData

    private var hotMealsCached: List<Hot>? = null

    fun getHotMeals() {
        if (hotMealsCached != null) {
            _getHotMealLiveData.postValue(hotMealsCached!!)
            return
        }
        viewModelScope.launch(IO) {
            try {
                val response = homeRepository.getHotMeals("Seafood")
                if (response.isSuccessful) {
                    val meals = response.body()?.meals
                    meals?.let {
                        _getHotMealLiveData.postValue(it)
                        hotMealsCached = it
                    }
                }
            } catch (t:Throwable) {
                Log.d("TAG", t.message.toString() + "HotMeal 에러")
            }
        }
    }

    private val _getCategoriesStateFlow = MutableStateFlow<List<Category>>(emptyList())
    val  getCategoriesStateFlow: StateFlow<List<Category>> = _getCategoriesStateFlow

    private var categoriesCached: List<Category>? = null

    fun getCategoriesHomeFragment() {
        categoriesCached?.let {
            _getCategoriesStateFlow.value = it
            return
        }
        viewModelScope.launch(IO) {
            try {
                val response = homeRepository.getCategoriesHome()
                if (response.isSuccessful) {
                    val categories = response.body()?.categories
                    categories?.let {
                        _getCategoriesStateFlow.emit(it)
                        categoriesCached = it
                    }
                }
            } catch (t:Throwable) {
                Log.d("TAG", t.message.toString() + "Category 에러")
            }
        }
    }
}