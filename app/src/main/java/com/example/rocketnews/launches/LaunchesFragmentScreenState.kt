package com.example.rocketnews.launches

import com.example.rocketnews.apiNasa.ResponseNasa
import com.example.rocketnews.apiSpaceX.ResponseSpaceXItem
import com.example.rocketnews.daily.DailyFragmentScreenState
import retrofit2.Response

sealed class LaunchesFragmentScreenState {
    data class Success(val data: List<ResponseSpaceXItem>) : LaunchesFragmentScreenState()
    data class Error(val throwable: Throwable) : LaunchesFragmentScreenState()
    object Loading : LaunchesFragmentScreenState()
}