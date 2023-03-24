package com.waveneuro.ui.dashboard.device.viewmodel

import androidx.lifecycle.LiveData
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.viewmodel.BaseViewModel
import com.waveneuro.ui.dashboard.device.DeviceViewEffect
import com.waveneuro.ui.dashboard.device.DeviceViewEvent
import com.waveneuro.ui.dashboard.device.DeviceViewState

interface DeviceViewModel : BaseViewModel {
    val data: LiveData<DeviceViewState>
    val viewEffect: SingleLiveEvent<DeviceViewEffect>
    val onboardingDisplayed: Boolean
    fun processEvent(viewEvent: DeviceViewEvent)
}