package com.waveneuro.ui.user.password.recovery;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.waveneuro.domain.base.SingleLiveEvent;

import javax.inject.Inject;

public class RecoveryInstructionsViewModel extends ViewModel {

    private final MutableLiveData<RecoveryInstructionsViewState> mDataLive = new MutableLiveData<>();
    private final SingleLiveEvent<RecoveryInstructionsViewEffect> mDataViewEffect = new SingleLiveEvent<>();

    @Inject
    public RecoveryInstructionsViewModel() {
    }

    void processEvent(RecoveryInstructionsViewEvent viewEvent) {
        if (viewEvent instanceof RecoveryInstructionsViewEvent.Start) {
        } else if (viewEvent instanceof RecoveryInstructionsViewEvent.BackClicked) {
            this.mDataViewEffect.postValue(new RecoveryInstructionsViewEffect.Back());
        } else if (viewEvent instanceof RecoveryInstructionsViewEvent.OpenEmailAppClicked) {
            this.mDataViewEffect.postValue(new RecoveryInstructionsViewEffect.EmailApp());
        } else if (viewEvent instanceof RecoveryInstructionsViewEvent.SkipClicked) {
            this.mDataViewEffect.postValue(new RecoveryInstructionsViewEffect.Skip());
        } else if (viewEvent instanceof RecoveryInstructionsViewEvent.ContactUsClicked) {
            this.mDataViewEffect.postValue(new RecoveryInstructionsViewEffect.ContactUs());
        }
    }

    public MutableLiveData<RecoveryInstructionsViewState> getData() {
        return mDataLive;
    }

    public SingleLiveEvent<RecoveryInstructionsViewEffect> getViewEffect() {
        return mDataViewEffect;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
