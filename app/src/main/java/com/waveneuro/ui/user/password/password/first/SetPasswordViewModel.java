package com.waveneuro.ui.user.password.password.first;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.asif.abase.data.model.APIError;
import com.asif.abase.domain.base.UseCaseCallback;
import com.asif.abase.exception.SomethingWrongException;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.password.password.SetPasswordRequest;
import com.waveneuro.data.model.response.password.password.SetPasswordResponse;
import com.waveneuro.domain.base.SingleLiveEvent;
import com.waveneuro.domain.usecase.password.SetPasswordUseCase;
import com.waveneuro.utils.ErrorUtil;

import javax.inject.Inject;

public class SetPasswordViewModel extends ViewModel {

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;

    private final MutableLiveData<SetPasswordViewState> mDataLive = new MutableLiveData<>();
    private final SingleLiveEvent<SetPasswordViewEffect> mDataViewEffect = new SingleLiveEvent<>();
    private final SetPasswordUseCase setPasswordUseCase;

    @Inject
    public SetPasswordViewModel(SetPasswordUseCase setPasswordUseCase) {
        this.setPasswordUseCase = setPasswordUseCase;
    }

    void processEvent(SetPasswordViewEvent viewEvent) {
        if (viewEvent instanceof SetPasswordViewEvent.Start) {
        } else if (viewEvent instanceof SetPasswordViewEvent.SetPassword) {
            SetPasswordViewEvent.SetPassword event = (SetPasswordViewEvent.SetPassword) viewEvent;
            setPassword(event.getUsername(), event.getNewPassword(), event.getCode());
        }
    }

    private void setPassword(String username, String password, String code) {
        SetPasswordRequest request = new SetPasswordRequest(username, password, code);
        this.setPasswordUseCase.execute(request, new UseCaseCallback() {
            @Override
            public void onSuccess(Object response) {
                SetPasswordResponse setPasswordResponse = (SetPasswordResponse) response;
                if (setPasswordResponse.getError() != null && TextUtils.isEmpty(setPasswordResponse.getError())) {
                    APIError error = errorUtil.parseError(new SomethingWrongException(), setPasswordResponse.getError());
                    mDataLive.postValue(new SetPasswordViewState.Failure(error));
                } else {
                    mDataLive.postValue(new SetPasswordViewState.Success());
                    mDataViewEffect.postValue(new SetPasswordViewEffect.SignInRedirect());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                APIError error = errorUtil.parseError(throwable);
                mDataLive.postValue(new SetPasswordViewState.Failure(error));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    public MutableLiveData<SetPasswordViewState> getData() {
        return mDataLive;
    }

    public SingleLiveEvent<SetPasswordViewEffect> getViewEffect() {
        return mDataViewEffect;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
