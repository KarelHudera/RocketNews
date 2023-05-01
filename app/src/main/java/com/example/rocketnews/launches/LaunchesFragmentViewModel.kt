package com.example.rocketnews.launches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rocketnews.apiSpaceX.ApiSpaceXData
import kotlinx.coroutines.launch

class LaunchesFragmentViewModel: ViewModel() {

    private val _screenState = MutableLiveData<LaunchesFragmentScreenState>()
    val screenState: LiveData<LaunchesFragmentScreenState> = _screenState

    init {
        getDataFromSpaceX()
    }

    private fun getDataFromSpaceX() {
        _screenState.value = LaunchesFragmentScreenState.Loading

        viewModelScope.launch {
            try {
                val responseSpaceX = ApiSpaceXData.getResponseSpaceX().body()!!
                _screenState.postValue(LaunchesFragmentScreenState.Success(responseSpaceX))
            } catch (exception: Exception) {
                _screenState.postValue(LaunchesFragmentScreenState.Error(exception))
            }
        }
    }

    fun refreshFragment() {
        getDataFromSpaceX()
    }
}