package com.waveneuro.ui.session.session;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.analytics.AnalyticsEvent;
import com.waveneuro.data.analytics.AnalyticsManager;
import com.waveneuro.data.model.request.treatment.AddTreatmentRequest;
import com.waveneuro.domain.base.SingleLiveEvent;
import com.waveneuro.domain.usecase.treatment.AddTreatmentUseCase;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import timber.log.Timber;

public class SessionViewModel extends ViewModel {

    @Inject
    AnalyticsManager analyticsManager;

    @Inject
    DataManager dataManager;

    private final MutableLiveData<SessionViewState> mDataLive = new MutableLiveData<>();
    private final SingleLiveEvent<SessionViewEffect> mDataViewEffect = new SingleLiveEvent<>();

    private final AddTreatmentUseCase addTreatmentUseCase;

    @Inject
    public SessionViewModel(AddTreatmentUseCase addTreatmentUseCase) {
        this.addTreatmentUseCase = addTreatmentUseCase;
    }

    void processEvent(SessionViewEvent viewEvent) {
        Timber.e("SESSION_EVENT :: %s", "" + (viewEvent.getClass().getSimpleName()));
        if (this.mDataLive.getValue() != null)
            Timber.e("SESSION_STATE :: %s", "" + (this.mDataLive.getValue().getClass().getSimpleName()));
        if (viewEvent instanceof SessionViewEvent.Start) {
            sentSessionEvent(AnalyticsEvent.SESSION_STARTED,
                    dataManager.getUser().getUsername(),
                    dataManager.getEegId(),
                    dataManager.getProtocolId(),
                    dataManager.getSonalId());
            if(this.mDataLive.getValue() instanceof SessionViewState.SessionPaused) {
                this.mDataLive.postValue(new SessionViewState.ResumeSession());
            } else {
//                this.mDataLive.postValue(new SessionViewState.Initializing());
                this.mDataLive.postValue(new SessionViewState.LocateDevice());
                this.mDataViewEffect.postValue(new SessionViewEffect.InitializeBle());
            }
        } else if (viewEvent instanceof SessionViewEvent.BackClicked) {
            this.mDataViewEffect.postValue(new SessionViewEffect.Back());
        } else if (viewEvent instanceof SessionViewEvent.InitializeDevice) {
//            this.mDataLive.postValue(new SessionViewState.Initializing());
            this.mDataLive.postValue(new SessionViewState.LocateDevice());
        } else if (viewEvent instanceof SessionViewEvent.LocatingDevice) {
            this.mDataLive.postValue(new SessionViewState.LocateDevice());
        } else if (viewEvent instanceof SessionViewEvent.StartSession) {
            this.mDataLive.postValue(new SessionViewState.SessionStarted());
        } else if (viewEvent instanceof SessionViewEvent.EndSession) {
            sentSessionEvent(AnalyticsEvent.SESSION_COMPLETED,
                    dataManager.getUser().getUsername(),
                    dataManager.getEegId(),
                    dataManager.getProtocolId(),
                    dataManager.getSonalId());
            this.mDataLive.postValue(new SessionViewState.SessionEnded());
            //DONE API call
            addTreatmentData();
        } else if (viewEvent instanceof SessionViewEvent.DeviceDisconnected) {
            this.mDataLive.postValue(new SessionViewState.DeviceDisconnected());
        } else if (viewEvent instanceof SessionViewEvent.DevicePaused) {
            sentSessionEvent(AnalyticsEvent.SESSION_PAUSED,
                    dataManager.getUser().getUsername(),
                    dataManager.getEegId(),
                    dataManager.getProtocolId(),
                    dataManager.getSonalId());
            this.mDataLive.postValue(new SessionViewState.SessionPaused());
        } else if (viewEvent instanceof SessionViewEvent.ResumeSession) {
            this.mDataLive.postValue(new SessionViewState.ResumeSession());
        } else if (viewEvent instanceof SessionViewEvent.DeviceError) {
            sentSessionEvent(AnalyticsEvent.SESSION_TERMINATED_EARLY,
                    dataManager.getUser().getUsername(),
                    dataManager.getEegId(),
                    dataManager.getProtocolId(),
                    dataManager.getSonalId());
            SessionViewEvent.DeviceError event = (SessionViewEvent.DeviceError) viewEvent;
            //DONE call API with true
            this.mDataLive.postValue(
                    new SessionViewState.ErrorSession(
                            event.getTitle(),
                            event.getMessage()
                    ));
            sendErrorTreatmentData();
        }
    }

    private void sendErrorTreatmentData() {
        AddTreatmentRequest request = new AddTreatmentRequest();
        request.setCompleted(false);
        request.setEegId(Long.parseLong(dataManager.getEegId()));
        request.setProtocolId(Long.parseLong(dataManager.getProtocolId()));
        request.setSonalId(Long.parseLong(dataManager.getSonalId().replaceAll("\\D+","")));
        this.addTreatmentUseCase.execute(request, new UseCaseCallback() {
            @Override
            public void onSuccess(Object o) {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void addTreatmentData() {
        AddTreatmentRequest request = new AddTreatmentRequest();
        request.setEegId(Long.parseLong(dataManager.getEegId()));
        request.setProtocolId(Long.parseLong(dataManager.getProtocolId()));
        request.setSonalId(Long.parseLong(dataManager.getSonalId().replaceAll("\\D+","")));
        this.addTreatmentUseCase.execute(request, new UseCaseCallback() {
            @Override
            public void onSuccess(Object o) {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void sentSessionEvent(String eventName, String username, String eggId, String protocolId, String sonalId) {
        JSONObject properties = new JSONObject();
        try {
            properties.put("username", username);
            properties.put("protocol_id", protocolId);
            properties.put("treatment_eeg_id", eggId);
            properties.put("sonal_id", sonalId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        analyticsManager.sendEvent(eventName, properties, AnalyticsManager.MIX_PANEL);
    }

    public MutableLiveData<SessionViewState> getData() {
        return mDataLive;
    }

    public SingleLiveEvent<SessionViewEffect> getViewEffect() {
        return mDataViewEffect;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}