package com.waveneuro.ui.dashboard.history.viewmodel

import android.app.Application
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.domain.usecase.user.GetSonalDevicesUseCase
import com.waveneuro.ui.base.handler.error.ErrorHandler
import com.waveneuro.ui.base.viewmodel.BaseAndroidViewModelImpl
import com.waveneuro.ui.dashboard.history.HistoryViewEffect
import com.waveneuro.ui.dashboard.history.HistoryViewEffect.Success
import com.waveneuro.ui.dashboard.history.HistoryViewEvent
import javax.inject.Inject

class HistoryViewModelImpl @Inject constructor(
    app: Application,
    errorHandler: ErrorHandler,
    private val getSonalDevicesUseCase: GetSonalDevicesUseCase
) : BaseAndroidViewModelImpl(app, errorHandler), HistoryViewModel {

    override val viewEffect = SingleLiveEvent<HistoryViewEffect>()

    override fun processEvent(viewEvent: HistoryViewEvent) {
        when (viewEvent) {
            is HistoryViewEvent.Start -> {
                getSonalDevices()
            }
        }
    }

    private fun getSonalDevices() {
        launchPayload {
            val response = getSonalDevicesUseCase.getSonalDevices()
            viewEffect.postValue(Success(response.devices))
        }
    }

}