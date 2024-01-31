package com.example.mealtoday.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealtoday.data.Category
import com.example.mealtoday.data.Hot
import com.example.mealtoday.data.Meal
import com.example.mealtoday.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
        viewModelScope.launch {
            try {
                val response = homeRepository.getRandomMeal()
                response.body()!!.meals.let {
                    _getRandomMealLiveData.postValue(it[0])
                }
                saveStateRandomMeal = response.body()!!.meals[0]
            } catch (t:Throwable) {
                Log.d("TAG", t.message.toString() + "RandomMeal 에러")
            }
        }
    }

    private val _getHotMealLiveData = MutableLiveData<List<Hot>>()
    val getHotMealLiveData: LiveData<List<Hot>> = _getHotMealLiveData

    fun getHotMeals() {
        viewModelScope.launch {
            try {
                val response = homeRepository.getHotMeals("Seafood")
                if (response.isSuccessful) {
                    response.body()?.meals?.let {
                        _getHotMealLiveData.postValue(it)
                    }
                }
            } catch (t:Throwable) {
                Log.d("TAG", t.message.toString() + "HotMeal 에러")
            }
        }
    }

    private val _getCategoriesStateFlow = MutableStateFlow<List<Category>>(emptyList())
    val  getCategoriesStateFlow: StateFlow<List<Category>> = _getCategoriesStateFlow

    fun getCategoriesHomeFragment() {
        viewModelScope.launch {
            try {
                val response = homeRepository.getCategoriesHome()
                if (response.isSuccessful) {
                    response.body()?.categories.let { data ->
                        if (data != null) {
                            _getCategoriesStateFlow.emit(data)
                        }
                    }
                }
            } catch (t:Throwable) {
                Log.d("TAG", t.message.toString() + "Category 에러")
            }
        }
    }
}