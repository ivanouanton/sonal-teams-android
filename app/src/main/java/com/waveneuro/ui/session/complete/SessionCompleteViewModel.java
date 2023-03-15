package com.waveneuro.ui.session.complete;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.session.AddTreatmentRequest;
import com.waveneuro.domain.base.SingleLiveEvent;
import com.waveneuro.utils.ErrorUtil;

import javax.inject.Inject;

public class SessionCompleteViewModel extends ViewModel {

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;

    private final MutableLiveData<SessionCompleteViewState> mDataLive = new MutableLiveData<>();
    private final SingleLiveEvent<SessionCompleteViewEffect> mDataViewEffect = new SingleLiveEvent<>();
    private final AddTreatmentUseCase addTreatmentUseCase;

    @Inject
    public SessionCompleteViewModel(AddTreatmentUseCase addTreatmentUseCase) {
        this.addTreatmentUseCase = addTreatmentUseCase;
    }

    void processEvent(SessionCompleteViewEvent viewEvent) {
        if (viewEvent instanceof SessionCompleteViewEvent.Start) {
            //completeSession();
        } else if (viewEvent instanceof SessionCompleteViewEvent.BackClicked) {
            this.mDataViewEffect.postValue(new SessionCompleteViewEffect.Back());
        } else if (viewEvent instanceof SessionCompleteViewEvent.HomeClicked) {
            this.mDataViewEffect.postValue(new SessionCompleteViewEffect.Home());
        }
    }

    private void completeSession() {
        AddTreatmentRequest request = new AddTreatmentRequest();
        request.setEegId(Long.parseLong(dataManager.getEegId()));
        request.setProtocolId(Long.parseLong(dataManager.getProtocolId()));
        request.setSonalId(dataManager.getSonalId());
        request.setFinishedAt(System.currentTimeMillis());
        addTreatmentUseCase.execute(request, new UseCaseCallback() {
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

    public MutableLiveData<SessionCompleteViewState> getData() {
        return mDataLive;
    }

    public SingleLiveEvent<SessionCompleteViewEffect> getViewEffect() {
        return mDataViewEffect;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}

