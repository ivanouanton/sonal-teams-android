package com.waveneuro.ui.session.session.viewmodel

import com.ap.ble.BluetoothManager
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.viewmodel.BaseViewModel
import com.waveneuro.ui.session.session.SessionViewEffect
import com.waveneuro.ui.session.session.SessionViewEvent
import kotlinx.coroutines.flow.StateFlow

interface SessionViewModel : BaseViewModel {
    val viewEffect: SingleLiveEvent<SessionViewEffect>
    val isCriticalDialogShowed: StateFlow<Boolean>
    val isLowDialogShowed: StateFlow<Boolean>
    val isPrepareDialogShowed: StateFlow<Boolean>
    var batteryLevelChangeCallback: BluetoothManager.OnBatteryLevelChangedCallback
    fun processEvent(viewEvent: SessionViewEvent)
}