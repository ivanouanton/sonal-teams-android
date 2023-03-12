package com.waveneuro.ui.dashboard.device

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.waveneuro.data.DataManager
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.domain.usecase.user.GetUserInfoUseCase
import com.waveneuro.ui.dashboard.device.DeviceViewEvent.*
import com.waveneuro.ui.dashboard.device.DeviceViewState.*
import com.waveneuro.utils.ErrorUtil
import timber.log.Timber
import javax.inject.Inject

class DeviceViewModel @Inject constructor(
    private val getPersonalInfoUseCase: GetUserInfoUseCase
) : ViewModel() {

    @Inject
    lateinit var errorUtil: ErrorUtil
    @Inject
    lateinit var dataManager: DataManager

    val data = MutableLiveData<DeviceViewState>()
    val viewEffect = SingleLiveEvent<DeviceViewEffect>()

    val onboardingDisplayed: Boolean
        get() = if (dataManager.onboardingDisplayed) {
            true
        } else {
            dataManager.setOnboardingDisplayed()
            false
        }

    fun processEvent(viewEvent: DeviceViewEvent) {
        Timber.e("DEVICE_EVENT :: %s", "" + viewEvent.javaClass.simpleName)
        if (data.value != null) Timber.e(
            "DEVICE_STATE :: %s",
            "" + data.value?.javaClass?.simpleName
        )
        when (viewEvent) {
            is Start -> data.postValue(InitLocateDevice(dataManager.user))
            is BackClicked -> {}
            is DeviceClicked -> data.postValue(Connecting(viewEvent.bleDevice))
            is DeviceViewEvent.LocateDevice -> {
                Timber.e("DEVICE_STATE :: %s", "" + (data.value is DeviceViewState.LocateDevice))
                data.postValue(Searching)
            }
            is DeviceViewEvent.Connected -> data.postValue(DeviceViewState.Connected)
            is Disconnected -> data.postValue(DeviceViewState.LocateDevice)
            is DeviceViewEvent.Searched -> data.postValue(DeviceViewState.Searched)
            is NoDeviceFound -> data.postValue(DeviceViewState.LocateDevice)
            is LocateDeviceNextClicked -> {
                //TODO Searching device screen
                if (data.value == null || data.value is InitLocateDevice
                    || data.value is DeviceViewState.LocateDevice
                ) data.postValue(LocateDeviceNext) else data.postValue(Searching)
            }
            is StartSessionClicked -> {
                viewEffect.postValue(
                    DeviceViewEffect.SessionRedirect(
                        dataManager.treatmentLength,
                        dataManager.protocolFrequency,
                        dataManager.sonalId
                    )
                )
            }
        }
    }

    fun setDeviceId(sonalId: String?) {
        dataManager.saveSonalId(sonalId)
    }

}