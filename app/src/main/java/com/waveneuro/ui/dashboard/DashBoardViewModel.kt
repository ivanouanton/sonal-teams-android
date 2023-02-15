package com.waveneuro.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.dashboard.DashboardViewEvent.*
import com.waveneuro.ui.dashboard.DashboardViewState.Connect
import com.waveneuro.ui.dashboard.DashboardViewState.Disconnect
import javax.inject.Inject

class DashBoardViewModel @Inject constructor() : ViewModel() {

    val data = MutableLiveData<DashboardViewState>()
    val viewEffect = SingleLiveEvent<DashboardViewEffect>()

    fun processEvent(viewEvent: DashboardViewEvent) {
        when (viewEvent) {
            is Connected -> {
                data.postValue(Connect(viewEvent.bleDevice))
            }
            is Disconnected -> {
                data.postValue(Disconnect)
            }
            is AccountClicked -> {
                viewEffect.postValue(DashboardViewEffect.Account)
            }
            is DeviceClicked -> {
                when (val connect = data.value) {
                    is Connect -> {
                        viewEffect.postValue(
                            DashboardViewEffect.Device(connect.data.name)
                        )
                    }
                    else -> {}
                }
            }
        }
    }
}