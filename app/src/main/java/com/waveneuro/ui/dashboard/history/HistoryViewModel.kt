package com.waveneuro.ui.dashboard.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asif.abase.domain.base.UseCaseCallback
import com.waveneuro.data.DataManager
import com.waveneuro.data.model.response.device.SonalDevicesResponse
import com.waveneuro.domain.usecase.user.GetSonalDevicesUseCase
import com.waveneuro.ui.dashboard.history.HistoryViewState.*
import com.waveneuro.utils.ErrorUtil
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val getSonalDevicesUseCase: GetSonalDevicesUseCase
) : ViewModel() {

    val data = MutableLiveData<HistoryViewState>()

    @Inject
    lateinit var errorUtil: ErrorUtil
    @Inject
    lateinit var dataManager: DataManager

    fun processEvent(viewEvent: HistoryViewEvent?) {
        if (viewEvent is HistoryViewEvent.Start) {
            getSonalDevices()
        }
    }

    private fun getSonalDevices() {
        getSonalDevicesUseCase.execute(object : UseCaseCallback<SonalDevicesResponse> {
            override fun onSuccess(response: SonalDevicesResponse) {
                data.value = (Success(response))
            }

            override fun onError(throwable: Throwable) {
                val error = errorUtil.parseError(throwable)
                data.postValue(Failure(error))
            }

            override fun onFinish() {
                data.postValue(Loading(false))
            }
        })
    }

}