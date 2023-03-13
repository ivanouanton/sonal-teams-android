package com.waveneuro.ui.dashboard

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.handler.error.ErrorHandler
import com.waveneuro.ui.base.viewmodel.BaseAndroidViewModelImpl
import com.waveneuro.ui.dashboard.DashboardViewEvent.*
import com.waveneuro.ui.dashboard.DashboardViewState.Connect
import com.waveneuro.ui.dashboard.DashboardViewState.Disconnect
import javax.inject.Inject

class DashBoardViewModelImpl @Inject constructor(
    app: Application,
    errorHandler: ErrorHandler
) : BaseAndroidViewModelImpl(app, errorHandler), DashboardViewModel {

    val data = MutableLiveData<DashboardViewState>()
    override val viewEffect = SingleLiveEvent<DashboardViewEffect>()

    override fun processEvent(viewEvent: DashboardViewEvent) {
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