package com.waveneuro.ui.session.session

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asif.abase.domain.base.UseCaseCallback
import com.waveneuro.data.DataManager
import com.waveneuro.data.analytics.AnalyticsEvent
import com.waveneuro.data.analytics.AnalyticsManager
import com.waveneuro.data.model.request.treatment.AddTreatmentRequest
import com.waveneuro.data.model.response.treatment.TreatmentResponse
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.domain.usecase.treatment.AddTreatmentUseCase
import com.waveneuro.ui.session.session.SessionViewEvent.*
import com.waveneuro.ui.session.session.SessionViewState.ErrorSession
import com.waveneuro.ui.session.session.SessionViewState.SessionPaused
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class SessionViewModel @Inject constructor(
    private val addTreatmentUseCase: AddTreatmentUseCase
) : ViewModel() {

    @Inject
    lateinit var analyticsManager: AnalyticsManager
    @Inject
    lateinit var dataManager: DataManager

    val data = MutableLiveData<SessionViewState?>()
    val viewEffect = SingleLiveEvent<SessionViewEffect>()

    fun processEvent(viewEvent: SessionViewEvent) {
        Timber.e("SESSION_EVENT :: %s", "" + viewEvent.javaClass.simpleName)
        if (data.value != null)
            Timber.e("SESSION_STATE :: %s", "" + data.value!!.javaClass.simpleName)
        if (viewEvent is Start) {
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
//                this.mDataLive.postValue(new SessionViewState.Initializing());
                data.postValue(SessionViewState.LocateDevice)
                viewEffect.postValue(SessionViewEffect.InitializeBle)
            }
        } else if (viewEvent is SessionViewEvent.BackClicked) {
            viewEffect.postValue(SessionViewEffect.Back)
        } else if (viewEvent is InitializeDevice) {
//            this.mDataLive.postValue(new SessionViewState.Initializing());
            data.postValue(SessionViewState.LocateDevice)
        } else if (viewEvent is LocatingDevice) {
            data.postValue(SessionViewState.LocateDevice)
        } else if (viewEvent is SessionViewEvent.StartSession) {
            data.postValue(SessionViewState.SessionStarted)
        } else if (viewEvent is EndSession) {
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
        } else if (viewEvent is SessionViewEvent.DeviceDisconnected) {
            data.postValue(SessionViewState.DeviceDisconnected)
        } else if (viewEvent is DevicePaused) {
            sentSessionEvent(
                AnalyticsEvent.SESSION_PAUSED,
                dataManager.user.id,
                dataManager.eegId,
                dataManager.protocolId,
                dataManager.sonalId
            )
            data.postValue(SessionPaused)
        } else if (viewEvent is SessionViewEvent.ResumeSession) {
            data.postValue(SessionViewState.ResumeSession)
        } else if (viewEvent is DeviceError) {
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

    override fun onCleared() {
        super.onCleared()
    }
}