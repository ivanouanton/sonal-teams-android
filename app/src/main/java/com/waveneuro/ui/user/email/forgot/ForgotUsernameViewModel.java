package com.waveneuro.ui.user.email.forgot;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.email.forgot.ForgotUsernameRequest;
import com.waveneuro.domain.base.SingleLiveEvent;
import com.waveneuro.domain.usecase.sentemail.ForgotUsernameUseCase;
import com.waveneuro.utils.ErrorUtil;

import javax.inject.Inject;

public class ForgotUsernameViewModel extends ViewModel {

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;

    private final MutableLiveData<ForgotUsernameViewState> mDataLive = new MutableLiveData<>();
    private final SingleLiveEvent<ForgotUsernameViewEffect> mDataViewEffect = new SingleLiveEvent<>();
    private final ForgotUsernameUseCase forgotUsernameUseCase;

    @Inject
    public ForgotUsernameViewModel(ForgotUsernameUseCase forgotUsernameUseCase) {
        this.forgotUsernameUseCase = forgotUsernameUseCase;
    }

    void processEvent(ForgotUsernameViewEvent viewEvent) {
        if (viewEvent instanceof ForgotUsernameViewEvent.Start) {
        } else if (viewEvent instanceof ForgotUsernameViewEvent.BackClicked) {
            this.mDataViewEffect.postValue(new ForgotUsernameViewEffect.BackRedirect());
        } else if (viewEvent instanceof ForgotUsernameViewEvent.ForgotUsernameClicked) {
            forgotUsername();
        } else if (viewEvent instanceof ForgotUsernameViewEvent.ForgotPasswordClicked) {
            this.mDataViewEffect.postValue(new ForgotUsernameViewEffect.ForgotPasswordRedirect());
        } else if (viewEvent instanceof ForgotUsernameViewEvent.RegisterClicked) {
            this.mDataViewEffect.postValue(new ForgotUsernameViewEffect.RegisterRedirect());
        } else if (viewEvent instanceof ForgotUsernameViewEvent.AboutUsClicked) {
            this.mDataViewEffect.postValue(new ForgotUsernameViewEffect.AboutUsRedirect());
        }
    }

    private void forgotUsername() {
        this.forgotUsernameUseCase.execute(new ForgotUsernameRequest(), new UseCaseCallback() {
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

    public MutableLiveData<ForgotUsernameViewState> getData() {
        return mDataLive;
    }

    public SingleLiveEvent<ForgotUsernameViewEffect> getViewEffect() {
        return mDataViewEffect;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
