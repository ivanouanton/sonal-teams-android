package com.waveneuro.ui.user.password.change;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.waveneuro.data.DataManager;
import com.waveneuro.domain.base.SingleLiveEvent;
import com.waveneuro.domain.usecase.password.ChangePasswordUseCase;
import com.waveneuro.utils.ErrorUtil;

import javax.inject.Inject;

public class ChangePasswordViewModel extends ViewModel {

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;

    private final MutableLiveData<ChangePasswordViewState> mDataLive = new MutableLiveData<>();
    private final SingleLiveEvent<ChangePasswordViewEffect> mDataViewEffect = new SingleLiveEvent<>();
    private final ChangePasswordUseCase changePasswordUseCase;

    @Inject
    public ChangePasswordViewModel(ChangePasswordUseCase changePasswordUseCase) {
        this.changePasswordUseCase = changePasswordUseCase;
    }

    void processEvent(ChangePasswordViewEvent viewEvent) {
        if (viewEvent instanceof ChangePasswordViewEvent.Start) {
        } else if (viewEvent instanceof ChangePasswordViewEvent.BackClicked) {
            this.mDataViewEffect.postValue(new ChangePasswordViewEffect.Back());
        }
    }



    public MutableLiveData<ChangePasswordViewState> getData() {
        return mDataLive;
    }

    public SingleLiveEvent<ChangePasswordViewEffect> getViewEffect() {
        return mDataViewEffect;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
