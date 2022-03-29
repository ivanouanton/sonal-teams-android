package com.waveneuro.ui.user.registration;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.waveneuro.data.DataManager;
import com.waveneuro.domain.base.SingleLiveEvent;
import com.waveneuro.domain.usecase.registration.RegistrationUseCase;
import com.waveneuro.utils.ErrorUtil;

import javax.inject.Inject;

public class RegistrationViewModel extends ViewModel {

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;

    private final MutableLiveData<RegistrationViewState> mDataLive = new MutableLiveData<>();
    private final SingleLiveEvent<RegistrationViewEffect> mDataViewEffect = new SingleLiveEvent<>();
    private final RegistrationUseCase registrationUseCase;

    @Inject
    public RegistrationViewModel(RegistrationUseCase registrationUseCase) {
        this.registrationUseCase = registrationUseCase;
    }

    void processEvent(RegistrationViewEvent viewEvent) {
        if (viewEvent instanceof RegistrationViewEvent.Start) {
        } else if (viewEvent instanceof RegistrationViewEvent.BackClicked) {
            this.mDataViewEffect.postValue(new RegistrationViewEffect.Back());
        }
    }

    public MutableLiveData<RegistrationViewState> getData() {
        return mDataLive;
    }

    public SingleLiveEvent<RegistrationViewEffect> getViewEffect() {
        return mDataViewEffect;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
