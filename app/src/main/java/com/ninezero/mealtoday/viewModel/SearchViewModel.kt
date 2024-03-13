package com.ninezero.mealtoday.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninezero.mealtoday.model.Meal
import com.ninezero.mealtoday.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
): ViewModel() {

    private val _getSearchMealLiveData = MutableLiveData<List<Meal>>()
    val getSearchMealLiveData: LiveData<List<Meal>> = _getSearchMealLiveData

    private var searchJob: Job? = null

    fun getSearchMeal(searchQuery: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            try {
                val response = searchRepository.getSearchMeal(searchQuery)
                if (response.isSuccessful) {
                    _getSearchMealLiveData.value = response.body()?.meals ?: emptyList()
                }
            } catch (t:Throwable) {
                Log.d("TAG", t.message.toString() + "SearchMeal 에러")
            }
        }
    }

    fun clearSearchResult() {
        _getSearchMealLiveData.value = emptyList()
    }
}