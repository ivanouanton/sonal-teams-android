package com.waveneuro.ui.user.password.code;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.asif.abase.data.model.APIError;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.analytics.AnalyticsManager;
import com.waveneuro.data.model.request.login.LoginRequest;
import com.waveneuro.data.model.request.password.password.ForgotPasswordRequest;
import com.waveneuro.data.model.response.login.LoginResponseMfa;
import com.waveneuro.data.preference.PreferenceManager;
import com.waveneuro.domain.base.SingleLiveEvent;
import com.waveneuro.domain.usecase.login.ConfirmTokenUseCase;
import com.waveneuro.domain.usecase.login.LoginUseCase;
import com.waveneuro.domain.usecase.password.ForgotPasswordUseCase;
import com.waveneuro.domain.usecase.user.GetPersonalInfoUseCase;
import com.waveneuro.ui.user.login.LoginViewEffect;
import com.waveneuro.ui.user.login.LoginViewEvent;
import com.waveneuro.ui.user.login.LoginViewState;
import com.waveneuro.utils.ErrorUtil;

import javax.inject.Inject;

public class ForgotPasswordCodeViewModel extends ViewModel {

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;

    @Inject
    PreferenceManager preferenceManager;

    @Inject
    AnalyticsManager analyticsManager;

    private final MutableLiveData<LoginViewState> mDataLive = new MutableLiveData<>();
    private final SingleLiveEvent<LoginViewEffect> mDataViewEffect = new SingleLiveEvent<>();

    private final LoginUseCase loginUseCase;
    private final GetPersonalInfoUseCase getPersonalInfoUseCase;
    private final ConfirmTokenUseCase confirmTokenUseCase;
    private final ForgotPasswordUseCase forgotPasswordUseCase;


    @Inject
    public ForgotPasswordCodeViewModel(LoginUseCase loginUseCase, GetPersonalInfoUseCase getPersonalInfoUseCase, ConfirmTokenUseCase confirmTokenUseCase, ForgotPasswordUseCase forgotPasswordUseCase) {
        this.loginUseCase = loginUseCase;
        this.getPersonalInfoUseCase = getPersonalInfoUseCase;
        this.confirmTokenUseCase = confirmTokenUseCase;
        this.forgotPasswordUseCase = forgotPasswordUseCase;
    }

    void processEvent(LoginViewEvent viewEvent) {
        if (viewEvent instanceof LoginViewEvent.Start) {
            isRememberDataExist();
        } else if (viewEvent instanceof LoginViewEvent.LoginClicked) {
            LoginViewEvent.LoginClicked loginClicked = (LoginViewEvent.LoginClicked) viewEvent;
            login(loginClicked.getUsername(), loginClicked.getPassword());
        } else if (viewEvent instanceof LoginViewEvent.ForgotPasswordClicked) {
            this.mDataViewEffect.postValue(new LoginViewEffect.ForgotPassword());
        } else if (viewEvent instanceof LoginViewEvent.RegisterClicked) {
            this.mDataViewEffect.postValue(new LoginViewEffect.Register());
        } else if (viewEvent instanceof LoginViewEvent.RememberUser) {
//            this.mDataViewEffect.postValue(new LoginViewEffect.RememberMe());
            LoginViewEvent.RememberUser rememberUser = (LoginViewEvent.RememberUser) viewEvent;
            saveUserLoginDetails(rememberUser.getUsername());
        } else if (viewEvent instanceof LoginViewEvent.ClearRememberUser) {
            removeRememberUserData();
        } else if (viewEvent instanceof LoginViewEvent.SupportClicked) {
            this.mDataViewEffect.postValue(new LoginViewEffect.Support());
        }
    }

    void resetPassword(String username) {
        this.forgotPasswordUseCase.execute(new ForgotPasswordRequest(username), new UseCaseCallback() {
            @Override
            public void onSuccess(Object response) {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void removeRememberUserData() {
        if (!TextUtils.isEmpty(dataManager.getRememberUsername())) {
            this.dataManager.removeRememberUser();
        }
        if (!TextUtils.isEmpty(dataManager.getRememberPassword())) {
            this.dataManager.removeRememberPassword();
        }
    }

    private void isRememberDataExist() {
        if (!TextUtils.isEmpty(dataManager.getRememberUsername())) {
            this.mDataViewEffect.postValue(
                    new LoginViewEffect.RememberMe(
                            dataManager.getRememberUsername()
                    ));
        }
    }

    private void saveUserLoginDetails(String username) {
        this.dataManager.rememberUsername(username);
    }

    private void login(String username, String password) {
        mDataLive.postValue(new LoginViewState.Loading(true));
        this.loginUseCase.execute(new LoginRequest(username, password), new UseCaseCallback<LoginResponseMfa>() {

            @Override
            public void onSuccess(LoginResponseMfa loginResponseMfa) {
                if (loginResponseMfa.getChallengeName() != null && loginResponseMfa.getChallengeName().equals("SOFTWARE_TOKEN_MFA")){
                    mDataViewEffect.postValue(new LoginViewEffect.EnterMfaCode(loginResponseMfa.getSession()));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                mDataLive.postValue(new LoginViewState.Loading(false));
                APIError error = errorUtil.parseError(throwable);
                mDataLive.postValue(new LoginViewState.Failure(error));
            }

            @Override
            public void onFinish() {

            }
        });
    }


    public MutableLiveData<LoginViewState> getData() {
        return mDataLive;
    }

    public SingleLiveEvent<LoginViewEffect> getViewEffect() {
        return mDataViewEffect;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

}