package com.example.rocketnews.daily

import com.example.rocketnews.api.ResponseNasa

sealed class DailyFragmentScreenState {
    data class Success(val data: ResponseNasa) : DailyFragmentScreenState()
    data class Error(val throwable: Throwable) : DailyFragmentScreenState()
    object Loading : DailyFragmentScreenState()
}
