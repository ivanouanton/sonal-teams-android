package com.waveneuro.ui.session.session

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ap.ble.BluetoothManager
import com.asif.abase.domain.base.UseCaseCallback
import com.waveneuro.data.DataManager
import com.waveneuro.data.analytics.AnalyticsEvent
import com.waveneuro.data.analytics.AnalyticsManager
import com.waveneuro.data.model.request.treatment.AddTreatmentRequest
import com.waveneuro.data.model.response.client.ClientResponse
import com.waveneuro.data.model.response.treatment.TreatmentResponse
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.domain.usecase.patient.GetPatientUseCase
import com.waveneuro.domain.usecase.treatment.AddTreatmentUseCase
import com.waveneuro.ui.session.session.SessionViewEvent.*
import com.waveneuro.ui.session.session.SessionViewState.ErrorSession
import com.waveneuro.ui.session.session.SessionViewState.SessionPaused
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class SessionViewModel @Inject constructor(
    private val addTreatmentUseCase: AddTreatmentUseCase,
    private val getPatientUseCase: GetPatientUseCase,
    private val dataManager: DataManager
) : ViewModel() {

    @Inject
    lateinit var analyticsManager: AnalyticsManager

    val data = MutableLiveData<SessionViewState?>()
    val viewEffect = SingleLiveEvent<SessionViewEffect>()
    val batteryLevel = MutableLiveData<Byte>()
    val currentClient = MutableLiveData<String>()

    var batteryLevelChangeCallback =
        BluetoothManager.OnBatteryLevelChangedCallback { newValue: Byte ->
            batteryLevel.value = newValue
        }

    init {
        getClient()
    }

    fun processEvent(viewEvent: SessionViewEvent) {
        Timber.e("SESSION_EVENT :: %s", "" + viewEvent.javaClass.simpleName)
        if (data.value != null)
            Timber.e("SESSION_STATE :: %s", "" + data.value!!.javaClass.simpleName)
        when (viewEvent) {
            is Start -> {
                sentSessionEvent(
                    AnalyticsEvent.SESSION_STARTED,
                    dataManager.user.id,
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
                addTreatmentData()
            }
            is DeviceDisconnected -> {
                data.postValue(SessionViewState.DeviceDisconnected)
            }
            is DevicePaused -> {
                sentSessionEvent(
                    AnalyticsEvent.SESSION_PAUSED,
                    dataManager.user.id,
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
                    dataManager.user.id,
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
                sendErrorTreatmentData()
            }
        }
    }

    private fun sendErrorTreatmentData() {
        val request = AddTreatmentRequest()
        request.eegId = dataManager.eegId.toLong()
        request.protocolId = dataManager.protocolId.toLong()
        request.sonalId = dataManager.sonalId
        request.isCompleted = false
        request.patientId = dataManager.patientId
        request.finishedAt = System.currentTimeMillis() / 1000
        addTreatmentUseCase.execute(request, object : UseCaseCallback<TreatmentResponse> {
            override fun onSuccess(response: TreatmentResponse) {}
            override fun onError(throwable: Throwable) {}
            override fun onFinish() {}
        })
    }

    private fun addTreatmentData() {
        val request = AddTreatmentRequest()
        request.eegId = dataManager.eegId.toLong()
        request.protocolId = dataManager.protocolId.toLong()
        request.sonalId = dataManager.sonalId
        request.isCompleted = true
        request.patientId = dataManager.patientId
        request.finishedAt = System.currentTimeMillis() / 1000
        addTreatmentUseCase.execute(request, object : UseCaseCallback<TreatmentResponse> {
            override fun onSuccess(response: TreatmentResponse) {}
            override fun onError(throwable: Throwable) {}
            override fun onFinish() {}
        })
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
        getPatientUseCase.execute(dataManager.patientId.toInt(), object : UseCaseCallback<ClientResponse?> {
            override fun onSuccess(response: ClientResponse?) {
                currentClient.value = ("${response?.firstName} ${response?.lastName}")
            }
            override fun onError(throwable: Throwable) {}
            override fun onFinish() {}
        })
    }

}