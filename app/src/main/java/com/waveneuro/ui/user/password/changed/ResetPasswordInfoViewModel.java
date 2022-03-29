package com.waveneuro.ui.user.password.changed;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.waveneuro.data.DataManager;
import com.waveneuro.domain.base.SingleLiveEvent;
import com.waveneuro.domain.usecase.password.ResetPasswordInfoUseCase;
import com.waveneuro.utils.ErrorUtil;

import javax.inject.Inject;

public class ResetPasswordInfoViewModel extends ViewModel {

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;

    private final MutableLiveData<ResetPasswordInfoViewState> mDataLive = new MutableLiveData<>();
    private final SingleLiveEvent<ResetPasswordInfoViewEffect> mDataViewEffect = new SingleLiveEvent<>();
    private final ResetPasswordInfoUseCase resetPasswordInfoUseCase;

    @Inject
    public ResetPasswordInfoViewModel(ResetPasswordInfoUseCase resetPasswordInfoUseCase) {
        this.resetPasswordInfoUseCase = resetPasswordInfoUseCase;
    }

    void processEvent(ResetPasswordInfoViewEvent viewEvent) {
        if (viewEvent instanceof ResetPasswordInfoViewEvent.Start) {
        } else if (viewEvent instanceof ResetPasswordInfoViewEvent.BackClicked) {
            this.mDataViewEffect.postValue(new ResetPasswordInfoViewEffect.Back());
        }
    }

    public MutableLiveData<ResetPasswordInfoViewState> getData() {
        return mDataLive;
    }

    public SingleLiveEvent<ResetPasswordInfoViewEffect> getViewEffect() {
        return mDataViewEffect;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

}
