package com.waveneuro.ui.user.login;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.asif.abase.data.model.APIError;
import com.asif.abase.data.model.BaseModel;
import com.asif.abase.domain.base.UseCaseCallback;
import com.asif.abase.exception.SomethingWrongException;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.analytics.AnalyticsEvent;
import com.waveneuro.data.analytics.AnalyticsManager;
import com.waveneuro.data.model.request.login.ConfirmTokenRequest;
import com.waveneuro.data.model.request.login.LoginRequest;
import com.waveneuro.data.model.response.login.ConfirmTokenResponse;
import com.waveneuro.data.model.response.login.LoginResponse;
import com.waveneuro.data.model.response.login.LoginResponseMfa;
import com.waveneuro.data.model.response.user.UserInfoResponse;
import com.waveneuro.domain.base.SingleLiveEvent;
import com.waveneuro.domain.usecase.login.ConfirmTokenUseCase;
import com.waveneuro.domain.usecase.login.LoginUseCase;
import com.waveneuro.domain.usecase.user.GetPersonalInfoUseCase;
import com.waveneuro.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import timber.log.Timber;

public class LoginViewModel extends ViewModel {

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;

    @Inject
    AnalyticsManager analyticsManager;

    private final MutableLiveData<LoginViewState> mDataLive = new MutableLiveData<>();
    private final SingleLiveEvent<LoginViewEffect> mDataViewEffect = new SingleLiveEvent<>();

    private final LoginUseCase loginUseCase;

    private final GetPersonalInfoUseCase getPersonalInfoUseCase;

    @Inject
    public LoginViewModel(LoginUseCase loginUseCase, GetPersonalInfoUseCase getPersonalInfoUseCase) {
        this.loginUseCase = loginUseCase;
        this.getPersonalInfoUseCase = getPersonalInfoUseCase;

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
                    mDataLive.postValue(new LoginViewState.Loading(false));
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

    private void sentLoginEvent(String username) {
        JSONObject properties = new JSONObject();
        try {
            properties.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        analyticsManager.sendEvent(AnalyticsEvent.LOGIN, properties, AnalyticsManager.MIX_PANEL);
    }

    private void getPersonalInfo(boolean firstEntrance) {
        this.getPersonalInfoUseCase.execute(new UseCaseCallback<UserInfoResponse>() {
            @Override
            public void onSuccess(UserInfoResponse response) {
                Timber.e("PROFILE_SUCCESS");
                mDataLive.postValue(new LoginViewState.Loading(false));

                dataManager.saveUser((UserInfoResponse) response);
                if (firstEntrance) {
                    mDataViewEffect.postValue(new LoginViewEffect.SetNewPassword());
                } else {
                    mDataLive.postValue(new LoginViewState.Success(new BaseModel()));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e("PROFILE_FAILURE");
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