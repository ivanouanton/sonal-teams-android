package com.waveneuro.ui.user.email.sent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.waveneuro.data.DataManager;
import com.waveneuro.domain.base.SingleLiveEvent;
import com.waveneuro.domain.usecase.sentemail.SentUsernameUseCase;
import com.waveneuro.utils.ErrorUtil;

import javax.inject.Inject;

public class SentUsernameViewModel extends ViewModel {

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;

    private final MutableLiveData<SentUsernameViewState> mDataLive = new MutableLiveData<>();
    private final SingleLiveEvent<SentUsernameViewEffect> mDataViewEffect = new SingleLiveEvent<>();
    private final SentUsernameUseCase sentUsernameUseCase;

    @Inject
    public SentUsernameViewModel(SentUsernameUseCase sentUsernameUseCase) {
        this.sentUsernameUseCase = sentUsernameUseCase;
    }

    void processEvent(SentUsernameViewEvent viewEvent) {
        if (viewEvent instanceof SentUsernameViewEvent.Start) {
        } else if (viewEvent instanceof SentUsernameViewEvent.BackClicked) {
            this.mDataViewEffect.postValue(new SentUsernameViewEffect.Back());
        }
    }

    public MutableLiveData<SentUsernameViewState> getData() {
        return mDataLive;
    }

    public SingleLiveEvent<SentUsernameViewEffect> getViewEffect() {
        return mDataViewEffect;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

}
