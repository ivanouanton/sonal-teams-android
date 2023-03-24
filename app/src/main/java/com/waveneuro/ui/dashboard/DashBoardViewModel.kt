package com.waveneuro.ui.dashboard

import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.viewmodel.BaseViewModel

interface DashboardViewModel : BaseViewModel {
    val viewEffect: SingleLiveEvent<DashboardViewEffect>
    fun processEvent(viewEvent: DashboardViewEvent)
}