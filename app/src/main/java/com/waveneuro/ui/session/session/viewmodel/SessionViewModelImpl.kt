package com.waveneuro.ui.session.session.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ap.ble.BluetoothManager
import com.waveneuro.data.DataManager
import com.waveneuro.data.analytics.AnalyticsEvent
import com.waveneuro.data.analytics.AnalyticsManager
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.domain.model.session.SessionRq
import com.waveneuro.domain.usecase.client.GetClientUseCase
import com.waveneuro.domain.usecase.session.AddSessionUseCase
import com.waveneuro.ui.base.handler.error.ErrorHandler
import com.waveneuro.ui.base.viewmodel.BaseAndroidViewModelImpl
import com.waveneuro.ui.session.session.SessionViewEffect
import com.waveneuro.ui.session.session.SessionViewEvent
import com.waveneuro.ui.session.session.SessionViewEvent.*
import com.waveneuro.ui.session.session.SessionViewState
import com.waveneuro.ui.session.session.SessionViewState.ErrorSession
import com.waveneuro.ui.session.session.SessionViewState.SessionPaused
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class SessionViewModelImpl @Inject constructor(
    app: Application,
    errorHandler: ErrorHandler,
    private val addSessionUseCase: AddSessionUseCase,
    private val getClientUseCase: GetClientUseCase,
    private val dataManager: DataManager
) : BaseAndroidViewModelImpl(app, errorHandler), SessionViewModel {

    @Inject
    lateinit var analyticsManager: AnalyticsManager

    override val viewEffect = SingleLiveEvent<SessionViewEffect>()
    override val isCriticalDialogShowed = MutableStateFlow(false)
    override val isLowDialogShowed = MutableStateFlow(false)
    override val isPrepareDialogShowed = MutableStateFlow(false)

    override var batteryLevelChangeCallback =
        BluetoothManager.OnBatteryLevelChangedCallback { newValue: Byte ->
            batteryLevel.value = newValue
        }

    val data = MutableLiveData<SessionViewState?>()
    val batteryLevel = MutableLiveData<Byte>()
    val currentClient = MutableLiveData<String>()

    init {
        getClient()
    }

    override fun processEvent(viewEvent: SessionViewEvent) {
        Timber.e("SESSION_EVENT :: %s", "" + viewEvent.javaClass.simpleName)
        if (data.value != null)
            Timber.e("SESSION_STATE :: %s", "" + data.value?.javaClass?.simpleName)
        when (viewEvent) {
            is Initializing -> {
                viewEffect.postValue(SessionViewEffect.ShowLoader)
                viewModelScope.launch(Dispatchers.IO) {
                    delay(DELAY)
                    viewEffect.postValue(SessionViewEffect.HideLoader)
                }
            }
            is Start -> {
                sentSessionEvent(
                    AnalyticsEvent.SESSION_STARTED,
                    "dataManager.user.id", //TODO uncommment
                    dataManager.eegId,
                    dataManager.protocolId,
                    dataManager.sonalId
                )
                if (data.value is SessionPaused) {
                    data.postValue(SessionViewState.ResumeSession)
                } else {
                    data.postValue(SessionViewState.LocateDevice)
                    viewEffect.postValue(SessionViewEffect.InitializeBle)
                }
                if (viewEvent.doNotShowAgain) {
                    dataManager.setPrecautionsDisplayed()
                }
            }
            is BackClicked -> {
                viewEffect.postValue(SessionViewEffect.Back)
            }
            is InitializeDevice -> {
                data.postValue(SessionViewState.LocateDevice)
            }
            is LocatingDevice -> {
                data.postValue(SessionViewState.LocateDevice)
            }
            is StartSessionClicked -> {
                data.postValue(SessionViewState.StartSessionClicked(
                    dataManager.isPrecautionsDisplayed
                ))
            }
            is StartSession -> {
                data.postValue(SessionViewState.SessionStarted)
            }
            is EndSession -> {
                sentSessionEvent(
                    AnalyticsEvent.SESSION_COMPLETED,
                    dataManager.user.id,
                    dataManager.eegId,
                    dataManager.protocolId,
                    dataManager.sonalId
                )
                data.postValue(SessionViewState.SessionEnded)
                //DONE API call
                addSessionData(isCompleted = true)
            }
            is DeviceDisconnected -> {
                data.postValue(SessionViewState.DeviceDisconnected)
            }
            is DevicePaused -> {
                sentSessionEvent(
                    AnalyticsEvent.SESSION_PAUSED,
                    "dataManager.user.id", //TODO uncommment
                    dataManager.eegId,
                    dataManager.protocolId,
                    dataManager.sonalId
                )
                data.postValue(SessionPaused)
            }
            is ResumeSession -> {
                data.postValue(SessionViewState.ResumeSession)
            }
            is DeviceError -> {
                sentSessionEvent(
                    AnalyticsEvent.SESSION_TERMINATED_EARLY,
                    "dataManager.user.id", //TODO uncommment
                    dataManager.eegId,
                    dataManager.protocolId,
                    dataManager.sonalId
                )
                val (title, message) = viewEvent
                //DONE call API with true
                data.postValue(
                    ErrorSession(
                        title,
                        message
                    )
                )
                addSessionData(isCompleted = false)
            }
        }
    }

    private fun addSessionData(isCompleted: Boolean) {
        val request = SessionRq()
        request.eegId = dataManager.eegId.toLong()
        request.protocolId = dataManager.protocolId.toLong()
        request.sonalId = dataManager.sonalId
        request.isCompleted = isCompleted
        request.patientId = dataManager.patientId
        request.finishedAt = System.currentTimeMillis() / 1000

        launchPayload {
            addSessionUseCase.addSession(request)
        }
    }

    private fun sentSessionEvent(
        eventName: String,
        userId: String,
        eggId: String,
        protocolId: String,
        sonalId: String
    ) {
        val properties = JSONObject()
        try {
            properties.put("user_id", userId)
            properties.put("protocol_id", protocolId)
            properties.put("treatment_eeg_id", eggId)
            properties.put("sonal_id", sonalId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        analyticsManager.sendEvent(eventName, properties, AnalyticsManager.MIX_PANEL)
    }

    private fun getClient() {
        launchPayload {
            val response = getClientUseCase.getClient(dataManager.patientId.toInt())
            currentClient.value = ("${response.firstName} ${response.lastName}")
        }
    }

    companion object {
        private const val DELAY = 10000L
    }

}