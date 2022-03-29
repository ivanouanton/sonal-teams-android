package com.waveneuro.ui.user.password.reset;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.asif.abase.data.model.APIError;
import com.asif.abase.domain.base.UseCaseCallback;
import com.asif.abase.exception.UserNotFoundException;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.password.password.ForgotPasswordRequest;
import com.waveneuro.data.model.response.password.password.ForgotPasswordResponse;
import com.waveneuro.domain.base.SingleLiveEvent;
import com.waveneuro.domain.usecase.password.ForgotPasswordUseCase;
import com.waveneuro.domain.usecase.password.ResetPasswordUseCase;
import com.waveneuro.utils.ErrorUtil;

import javax.inject.Inject;

public class ResetPasswordViewModel extends ViewModel {

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;

    private final MutableLiveData<ResetPasswordViewState> mDataLive = new MutableLiveData<>();
    private final SingleLiveEvent<ResetPasswordViewEffect> mDataViewEffect = new SingleLiveEvent<>();
    private final ResetPasswordUseCase resetPasswordUseCase;
    private final ForgotPasswordUseCase forgotPasswordUseCase;

    @Inject
    public ResetPasswordViewModel(ResetPasswordUseCase resetPasswordUseCase,
                                  ForgotPasswordUseCase forgotPasswordUseCase) {
        this.resetPasswordUseCase = resetPasswordUseCase;
        this.forgotPasswordUseCase = forgotPasswordUseCase;
    }

    void processEvent(ResetPasswordViewEvent viewEvent) {
        if (viewEvent instanceof ResetPasswordViewEvent.Start) {
        } else if (viewEvent instanceof ResetPasswordViewEvent.ForgotPasswordClicked) {
            ResetPasswordViewEvent.ForgotPasswordClicked forgotPasswordClicked = (ResetPasswordViewEvent.ForgotPasswordClicked) viewEvent;
            resetPassword(forgotPasswordClicked.getUsername());
        } else if (viewEvent instanceof ResetPasswordViewEvent.BackClicked) {
            this.mDataViewEffect.postValue(new ResetPasswordViewEffect.BackRedirect());
        } else if (viewEvent instanceof ResetPasswordViewEvent.ForgotUsernameClicked) {
            this.mDataViewEffect.postValue(new ResetPasswordViewEffect.ForgotUsernameRedirect());
        } else if (viewEvent instanceof ResetPasswordViewEvent.RegisterClicked) {
            this.mDataViewEffect.postValue(new ResetPasswordViewEffect.RegisterRedirect());
        } else if (viewEvent instanceof ResetPasswordViewEvent.AboutUsClicked) {
            this.mDataViewEffect.postValue(new ResetPasswordViewEffect.AboutUsRedirect());
        } else if (viewEvent instanceof ResetPasswordViewEvent.LoginClicked) {
            this.mDataViewEffect.postValue(new ResetPasswordViewEffect.LoginRedirect());
        }
    }

    private void resetPassword(String username) {
        this.forgotPasswordUseCase.execute(new ForgotPasswordRequest(username), new UseCaseCallback() {
            @Override
            public void onSuccess(Object response) {
                ForgotPasswordResponse forgotPasswordResponse = (ForgotPasswordResponse) response;
                if (forgotPasswordResponse.getError() != null
                        && TextUtils.isEmpty(forgotPasswordResponse.getError())) {
                    APIError error = errorUtil.parseError(new UserNotFoundException(), "User not found.");
                    mDataLive.postValue(new ResetPasswordViewState.Failure(error));
                } else {
                    mDataLive.postValue(new ResetPasswordViewState.Success());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                APIError error = errorUtil.parseError(throwable);
                mDataLive.postValue(new ResetPasswordViewState.Failure(error));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    public MutableLiveData<ResetPasswordViewState> getData() {
        return mDataLive;
    }

    public SingleLiveEvent<ResetPasswordViewEffect> getViewEffect() {
        return mDataViewEffect;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
