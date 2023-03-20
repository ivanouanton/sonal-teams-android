package com.waveneuro.ui.dashboard.home.viewmodel

import androidx.lifecycle.LiveData
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.viewmodel.BaseViewModel
import com.waveneuro.ui.dashboard.home.HomeViewEffect
import com.waveneuro.ui.dashboard.home.HomeViewEvent

interface HomeViewModel : BaseViewModel {
    val viewEffect: SingleLiveEvent<HomeViewEffect>
    val filters: LiveData<List<Int>>
    fun processEvent(viewEvent: HomeViewEvent)
}