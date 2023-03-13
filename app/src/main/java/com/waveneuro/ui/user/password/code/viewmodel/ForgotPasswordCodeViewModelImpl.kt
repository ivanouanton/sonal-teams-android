package com.waveneuro.ui.user.password.code.viewmodel

import android.app.Application
import android.text.TextUtils
import com.waveneuro.data.DataManager
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.domain.usecase.login.LoginUseCase
import com.waveneuro.domain.usecase.password.ForgotPasswordUseCase
import com.waveneuro.ui.base.handler.error.ErrorHandler
import com.waveneuro.ui.base.viewmodel.BaseAndroidViewModelImpl
import com.waveneuro.ui.user.password.code.ForgotPasswordCodeViewEffect
import com.waveneuro.ui.user.password.code.ForgotPasswordCodeViewEvent
import com.waveneuro.ui.user.password.code.ForgotPasswordCodeViewEvent.*
import javax.inject.Inject

class ForgotPasswordCodeViewModelImpl @Inject constructor(
    app: Application,
    errorHandler: ErrorHandler,
    private val loginUseCase: LoginUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : BaseAndroidViewModelImpl(app, errorHandler), ForgotPasswordCodeViewModel {

    @Inject
    lateinit var dataManager: DataManager

    override val viewEffect = SingleLiveEvent<ForgotPasswordCodeViewEffect>()

    override fun processEvent(viewEvent: ForgotPasswordCodeViewEvent) {
        when (viewEvent) {
            is Start -> {
                isRememberDataExist()
            }
            is LoginClicked -> {
                login(viewEvent.username, viewEvent.password)
            }
            is ForgotPasswordClicked -> {
                viewEffect.postValue(ForgotPasswordCodeViewEffect.ForgotPassword)
            }
            is RegisterClicked -> {
                viewEffect.postValue(ForgotPasswordCodeViewEffect.Register)
            }
            is RememberUser -> {
                saveUserLoginDetails(viewEvent.username)
            }
            is ClearRememberUser -> {
                removeRememberUserData()
            }
            is SupportClicked -> {
                viewEffect.postValue(ForgotPasswordCodeViewEffect.Support)
            }
        }
    }

    fun resetPassword(username: String?) {
        launchPayload {
            username?.let { forgotPasswordUseCase.forgotPassword(it) }
        }
    }

    private fun removeRememberUserData() {
        if (!TextUtils.isEmpty(dataManager.rememberUsername)) {
            dataManager.removeRememberUser()
        }
        if (!TextUtils.isEmpty(dataManager.rememberPassword)) {
            dataManager.removeRememberPassword()
        }
    }

    private fun isRememberDataExist() {
        if (!TextUtils.isEmpty(dataManager.rememberUsername)) {
            viewEffect.postValue(
                ForgotPasswordCodeViewEffect.RememberMe(dataManager.rememberUsername)
            )
        }
    }

    private fun saveUserLoginDetails(username: String) {
        dataManager.rememberUsername(username)
    }

    private fun login(username: String, password: String) {
        launchPayload {
            val response = loginUseCase.login(username, password)
            viewEffect.postValue(ForgotPasswordCodeViewEffect.EnterMfaCode(response.session))
        }
    }

}

//public class ForgotPasswordCodeViewModelImpl extends ViewModel {
//
//    @Inject
//    DataManager dataManager;
//
//    private final MutableLiveData<LoginViewState> mDataLive = new MutableLiveData<>();
//    private final SingleLiveEvent<LoginViewEffect> mDataViewEffect = new SingleLiveEvent<>();
//
//    private final LoginUseCase loginUseCase;
//    private final GetUserInfoUseCase getUserInfoUseCase;
//    private final ConfirmTokenUseCase confirmTokenUseCase;
//    private final ForgotPasswordUseCase forgotPasswordUseCase;
//
//
//    @Inject
//    public ForgotPasswordCodeViewModelImpl(LoginUseCase loginUseCase, GetUserInfoUseCase getUserInfoUseCase, ConfirmTokenUseCase confirmTokenUseCase, ForgotPasswordUseCase forgotPasswordUseCase) {
//        this.loginUseCase = loginUseCase;
//        this.getUserInfoUseCase = getUserInfoUseCase;
//        this.confirmTokenUseCase = confirmTokenUseCase;
//        this.forgotPasswordUseCase = forgotPasswordUseCase;
//    }
//
//    void processEvent(LoginViewEvent viewEvent) {
//        if (viewEvent instanceof LoginViewEvent.Start) {
//            isRememberDataExist();
//        } else if (viewEvent instanceof LoginViewEvent.LoginClicked) {
//            LoginViewEvent.LoginClicked loginClicked = (LoginViewEvent.LoginClicked) viewEvent;
//            login(loginClicked.getUsername(), loginClicked.getPassword());
//        } else if (viewEvent instanceof LoginViewEvent.ForgotPasswordClicked) {
//            this.mDataViewEffect.postValue(LoginViewEffect.ForgotPassword.INSTANCE);
//        } else if (viewEvent instanceof LoginViewEvent.RegisterClicked) {
//            this.mDataViewEffect.postValue(LoginViewEffect.Register.INSTANCE);
//        } else if (viewEvent instanceof LoginViewEvent.RememberUser) {
////            this.mDataViewEffect.postValue(new LoginViewEffect.RememberMe());
//            LoginViewEvent.RememberUser rememberUser = (LoginViewEvent.RememberUser) viewEvent;
//            saveUserLoginDetails(rememberUser.getUsername());
//        } else if (viewEvent instanceof LoginViewEvent.ClearRememberUser) {
//            removeRememberUserData();
//        } else if (viewEvent instanceof LoginViewEvent.SupportClicked) {
//            this.mDataViewEffect.postValue(LoginViewEffect.Support.INSTANCE);
//        }
//    }
//
//    void resetPassword(String username) {
//        this.forgotPasswordUseCase.execute(new ForgotPasswordRequest(username), new UseCaseCallback() {
//            @Override
//            public void onSuccess(Object response) {
//
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        });
//    }
//
//    private void removeRememberUserData() {
//        if (!TextUtils.isEmpty(dataManager.getRememberUsername())) {
//            this.dataManager.removeRememberUser();
//        }
//        if (!TextUtils.isEmpty(dataManager.getRememberPassword())) {
//            this.dataManager.removeRememberPassword();
//        }
//    }
//
//    private void isRememberDataExist() {
//        if (!TextUtils.isEmpty(dataManager.getRememberUsername())) {
//            this.mDataViewEffect.postValue(
//                new LoginViewEffect.RememberMe(
//                        dataManager.getRememberUsername()
//                        ));
//        }
//    }
//
//    private void saveUserLoginDetails(String username) {
//        this.dataManager.rememberUsername(username);
//    }
//
//    private void login(String username, String password) {
//        mDataLive.postValue(new LoginViewState.Loading(true));
//        this.loginUseCase.execute(new LoginRequest(username, password), new UseCaseCallback<ApiLoginResponseMfa>() {
//
//        @Override
//        public void onSuccess(ApiLoginResponseMfa loginResponseMfa) {
//            if (loginResponseMfa.getChallengeName() != null && loginResponseMfa.getChallengeName().equals("SOFTWARE_TOKEN_MFA")){
//                mDataViewEffect.postValue(new LoginViewEffect.EnterMfaCode(loginResponseMfa.getSession()));
//            }
//        }
//
//        @Override
//        public void onError(Throwable throwable) {
//            mDataLive.postValue(new LoginViewState.Loading(false));
//            APIError error = errorUtil.parseError(throwable);
//            mDataLive.postValue(new LoginViewState.Failure(error));
//        }
//
//        @Override
//        public void onFinish() {
//
//        }
//    });
//    }
//
//
//    public MutableLiveData<LoginViewState> getData() {
//        return mDataLive;
//    }
//
//    public SingleLiveEvent<LoginViewEffect> getViewEffect() {
//        return mDataViewEffect;
//    }
//
//    @Override
//    protected void onCleared() {
//        super.onCleared();
//    }
//
//}