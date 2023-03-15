package com.waveneuro.ui.dashboard.device.viewmodel

import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.viewmodel.BaseViewModel
import com.waveneuro.ui.dashboard.device.DeviceViewEffect
import com.waveneuro.ui.dashboard.device.DeviceViewEvent

interface DeviceViewModel : BaseViewModel {
    val viewEffect: SingleLiveEvent<DeviceViewEffect>
    fun processEvent(viewEvent: DeviceViewEvent)
}