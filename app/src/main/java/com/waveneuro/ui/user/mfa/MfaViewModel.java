package com.waveneuro.ui.user.mfa;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.asif.abase.data.model.APIError;
import com.asif.abase.data.model.BaseModel;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.analytics.AnalyticsEvent;
import com.waveneuro.data.analytics.AnalyticsManager;
import com.waveneuro.data.model.request.login.ConfirmTokenRequest;
import com.waveneuro.data.model.request.login.LoginRequest;
import com.waveneuro.data.model.response.login.ConfirmTokenResponse;
import com.waveneuro.data.model.response.login.LoginResponseMfa;
import com.waveneuro.data.model.response.user.UserInfoResponse;
import com.waveneuro.data.preference.PreferenceManager;
import com.waveneuro.domain.base.SingleLiveEvent;
import com.waveneuro.domain.usecase.login.ConfirmTokenUseCase;
import com.waveneuro.domain.usecase.login.LoginUseCase;
import com.waveneuro.domain.usecase.user.GetPersonalInfoUseCase;
import com.waveneuro.ui.user.login.LoginViewEffect;
import com.waveneuro.ui.user.login.LoginViewEvent;
import com.waveneuro.ui.user.login.LoginViewState;
import com.waveneuro.utils.ErrorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import timber.log.Timber;

public class MfaViewModel extends ViewModel {

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

    private final ConfirmTokenUseCase confirmTokenUseCase;

    @Inject
    public MfaViewModel(ConfirmTokenUseCase confirmTokenUseCase, ConfirmTokenUseCase confirmTokenUseCase2) {
        this.confirmTokenUseCase = confirmTokenUseCase;
    }

    public void confirmToken(String mfaCode, String username, String session) {
        this.confirmTokenUseCase.execute(new ConfirmTokenRequest(username, mfaCode, session), new UseCaseCallback<ConfirmTokenResponse>() {

            @Override
            public void onSuccess(ConfirmTokenResponse confirmTokenResponse) {
                mDataViewEffect.postValue(new LoginViewEffect.Home());
                preferenceManager.setAccessToken(confirmTokenResponse.getIdToken());
            }

            @Override
            public void onError(Throwable throwable) {
                mDataViewEffect.postValue(new LoginViewEffect.WrongMfaCode());
            }

            @Override
            public void onFinish() {
                int x = 0;
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