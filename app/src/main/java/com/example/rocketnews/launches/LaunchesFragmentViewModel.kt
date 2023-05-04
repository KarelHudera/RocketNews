package com.example.rocketnews.launches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rocketnews.apiSpaceX.ApiSpaceXData
import com.example.rocketnews.apiSpaceX.ResponseSpaceXItem
import kotlinx.coroutines.launch

class LaunchesFragmentViewModel: ViewModel() {

    private val _screenState = MutableLiveData<LaunchesFragmentScreenState>()
    val screenState: LiveData<LaunchesFragmentScreenState> = _screenState

    val _exchangesList = MutableLiveData<List<ResponseSpaceXItem>>()
    val exchangesList: LiveData<List<ResponseSpaceXItem>> = _exchangesList

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

    fun onTextChanged(query: String) {
        val state = screenState.value
        if (state is LaunchesFragmentScreenState.Success) {
            val filteredList = state.data.filter {
                val nameMatches = it.name.orEmpty().lowercase().startsWith(query.trim().lowercase())
                val payloadsMatch = it.payloads.orEmpty().any { payload ->
                    payload.lowercase().contains(query.trim().lowercase())
                }
                nameMatches || payloadsMatch
            }
            _exchangesList.value = filteredList
        }
    }
}