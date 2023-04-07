package com.waveneuro.ui.dashboard.device.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.waveneuro.data.preference.PreferenceManagerImpl
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.handler.error.ErrorHandler
import com.waveneuro.ui.base.viewmodel.BaseAndroidViewModelImpl
import com.waveneuro.ui.dashboard.device.DeviceViewEffect
import com.waveneuro.ui.dashboard.device.DeviceViewEvent
import com.waveneuro.ui.dashboard.device.DeviceViewEvent.*
import com.waveneuro.ui.dashboard.device.DeviceViewState
import com.waveneuro.ui.dashboard.device.DeviceViewState.*
import timber.log.Timber
import javax.inject.Inject

class DeviceViewModelImpl @Inject constructor(
    app: Application,
    errorHandler: ErrorHandler
) : BaseAndroidViewModelImpl(app, errorHandler), DeviceViewModel {

    private val prefs = PreferenceManagerImpl(appCtx)

    override val data = MutableLiveData<DeviceViewState>()
    override val viewEffect = SingleLiveEvent<DeviceViewEffect>()

    override val onboardingDisplayed: Boolean
        get() = if (prefs.isOnboardingDisplayed) {
            true
        } else {
            prefs.isOnboardingDisplayed = true
            false
        }

    override fun processEvent(viewEvent: DeviceViewEvent) {
        Timber.e("DEVICE_EVENT :: %s", viewEvent.javaClass.simpleName)
        if (data.value != null) Timber.e(
            "DEVICE_STATE :: %s",
            data.value?.javaClass?.simpleName
        )
        when (viewEvent) {
            is Start -> data.postValue(InitLocateDevice(prefs.user))
            is DeviceClicked -> data.postValue(Connecting(viewEvent.bleDevice))
            is DeviceViewEvent.LocateDevice -> {
                Timber.e("DEVICE_STATE :: %s", data.value is DeviceViewState.LocateDevice)
                data.postValue(Searching)
            }
            is DeviceViewEvent.Connected -> data.postValue(DeviceViewState.Connected)
            is Disconnected -> data.postValue(DeviceViewState.LocateDevice)
            is DeviceViewEvent.Searched -> data.postValue(DeviceViewState.Searched)
            is NoDeviceFound -> data.postValue(DeviceViewState.LocateDevice)
            is LocateDeviceNextClicked -> {
                if (data.value == null || data.value is InitLocateDevice
                    || data.value is DeviceViewState.LocateDevice
                ) data.postValue(LocateDeviceNext) else data.postValue(Searching)
            }
            is StartSessionClicked -> {
                viewEffect.postValue(
                    DeviceViewEffect.SessionRedirect(
                        prefs.treatmentLength ?: "",
                        prefs.protocolFrequency ?: "",
                        prefs.sonalId ?: ""
                    )
                )
            }
            is SetDeviceId -> {
                setDeviceId(viewEvent.deviceId)
            }
        }
    }

    private fun setDeviceId(sonalId: String?) {
        prefs.sonalId = sonalId
    }

}
