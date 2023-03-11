package com.waveneuro.ui.user.password.new_password;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.asif.abase.data.model.APIError;
import com.asif.abase.domain.base.UseCaseCallback;
import com.asif.abase.exception.SomethingWrongException;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.password.password.SetNewPasswordRequest;
import com.waveneuro.data.model.response.password.password.SetNewPasswordResponse;
import com.waveneuro.domain.base.SingleLiveEvent;
import com.waveneuro.domain.usecase.password.SetNewPasswordUseCase;
import com.waveneuro.utils.ErrorUtil;

import javax.inject.Inject;

public class SetNewPasswordViewModel extends ViewModel {

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;

    private final MutableLiveData<SetNewPasswordViewState> mDataLive = new MutableLiveData<>();
    private final SingleLiveEvent<SetNewPasswordViewEffect> mDataViewEffect = new SingleLiveEvent<>();
    private final SetNewPasswordUseCase setNewPasswordUseCase;

    @Inject
    public SetNewPasswordViewModel(SetNewPasswordUseCase setNewPasswordUseCase) {
        this.setNewPasswordUseCase = setNewPasswordUseCase;
    }

    void processEvent(SetNewPasswordViewEvent viewEvent) {
        if (viewEvent instanceof SetNewPasswordViewEvent.Start) {
        } else if (viewEvent instanceof SetNewPasswordViewEvent.SetNewPassword) {
            SetNewPasswordViewEvent.SetNewPassword event = (SetNewPasswordViewEvent.SetNewPassword) viewEvent;
            setNewPassword(event.getEmail(), event.getCode(), event.getPassword());
        }
    }

    private void setNewPassword(String email, String code, String password) {
        SetNewPasswordRequest request = new SetNewPasswordRequest(email, code, password);
        this.setNewPasswordUseCase.execute(request, new UseCaseCallback() {
            @Override
            public void onSuccess(Object response) {
                SetNewPasswordResponse setNewPasswordResponse = (SetNewPasswordResponse) response;
                if (setNewPasswordResponse.getError() != null && TextUtils.isEmpty(setNewPasswordResponse.getError())) {
                    APIError error = errorUtil.parseError(new SomethingWrongException(), setNewPasswordResponse.getError());
                    mDataLive.postValue(new SetNewPasswordViewState.Failure(error));
                } else {
                    mDataLive.postValue(new SetNewPasswordViewState.Success());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                APIError error = errorUtil.parseError(throwable);
                mDataLive.postValue(new SetNewPasswordViewState.Failure(error));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    public MutableLiveData<SetNewPasswordViewState> getData() {
        return mDataLive;
    }

    public SingleLiveEvent<SetNewPasswordViewEffect> getViewEffect() {
        return mDataViewEffect;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}

