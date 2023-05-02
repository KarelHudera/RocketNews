package com.example.rocketnews.daily

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rocketnews.App
import com.example.rocketnews.apiNasa.ApiNasaData.getResponseNasa
import com.example.rocketnews.databaseNasa.NasaItem
import kotlinx.coroutines.launch

class DailyFragmentViewModel: ViewModel() {

    private val _screenState = MutableLiveData<DailyFragmentScreenState>()
    val screenState: LiveData<DailyFragmentScreenState> = _screenState
    private val apiKey = "428D3IouMHvoxKD8eaefoZKwe0w2Syv3t5eFbklA"
    private val appDatabase = App.instance.databaseNasa


    init {
        getDataFromNasa()
    }

    private fun getDataFromNasa() {
        _screenState.value = DailyFragmentScreenState.Loading

        viewModelScope.launch {
            try {
                val responseNasa = getResponseNasa(apiKey).body()!!
                _screenState.postValue(DailyFragmentScreenState.Success(responseNasa))

                // Delete old data
                appDatabase.nasaItemDao().deleteAll()

                // Insert new data
                val nasaItem = NasaItem(
                    date = responseNasa.date,
                    explanation = responseNasa.explanation,
                    hdUrl = responseNasa.hdurl,
                    title = responseNasa.title
                )
                appDatabase.nasaItemDao().insert(nasaItem)

            } catch (exception: Exception) {
                _screenState.postValue(DailyFragmentScreenState.Error(exception))
            }
        }
    }

    fun refreshFragment() {
        getDataFromNasa()
    }
}