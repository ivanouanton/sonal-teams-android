package com.waveneuro.ui.user.login.viewmodel

import android.app.Application
import android.text.TextUtils
import com.waveneuro.data.DataManager
import com.waveneuro.data.analytics.AnalyticsManager
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.domain.usecase.login.LoginUseCase
import com.waveneuro.ui.base.handler.error.ErrorHandler
import com.waveneuro.ui.base.viewmodel.BaseAndroidViewModelImpl
import com.waveneuro.ui.user.login.LoginViewEffect
import com.waveneuro.ui.user.login.LoginViewEffect.EnterMfaCode
import com.waveneuro.ui.user.login.LoginViewEffect.RememberMe
import com.waveneuro.ui.user.login.LoginViewEvent
import com.waveneuro.ui.user.login.LoginViewEvent.*
import javax.inject.Inject

class LoginViewModelImpl @Inject constructor(
    app: Application,
    errorHandler: ErrorHandler,
    private val loginUseCase: LoginUseCase
) : BaseAndroidViewModelImpl(app, errorHandler), LoginViewModel {

    @Inject
    lateinit var dataManager: DataManager

    @Inject
    lateinit var analyticsManager: AnalyticsManager

    override val viewEffect = SingleLiveEvent<LoginViewEffect>()

    override fun processEvent(viewEvent: LoginViewEvent) {
        when (viewEvent) {
            is Start -> {
                isRememberDataExist()
            }
            is LoginClicked -> {
                login(viewEvent.username, viewEvent.password)
            }
            is ForgotPasswordClicked -> {
                viewEffect.postValue(LoginViewEffect.ForgotPassword)
            }
            is RegisterClicked -> {
                viewEffect.postValue(LoginViewEffect.Register)
            }
            is RememberUser -> {
                saveUserLoginDetails(viewEvent.username)
            }
            is ClearRememberUser -> {
                removeRememberUserData()
            }
            is SupportClicked -> {
                viewEffect.postValue(LoginViewEffect.Support)
            }
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
                    RememberMe(
                        dataManager.rememberUsername
                    )
                )
            }
        }

    private fun saveUserLoginDetails(username: String) {
        dataManager.rememberUsername(username)
    }

    private fun login(username: String, password: String) {
        launchPayload {
            val response = loginUseCase.login(username, password)

            if (response.challengeName != null) {
                when (response.challengeName) {
                    "SOFTWARE_TOKEN_MFA" -> {
                        viewEffect.postValue(EnterMfaCode(response.session))
                    }
                    "NEW_PASSWORD_REQUIRED", "MFA_SETUP" -> {
                        viewEffect.postValue(LoginViewEffect.ShowErrorDialog(null, "Set up MFA to your account"))
                    }
                    else -> {
                        viewEffect.postValue(LoginViewEffect.ShowErrorDialog(null, "Something went wrong…"))
                    }
                }
            } else {
                viewEffect.postValue(LoginViewEffect.ShowErrorDialog(null, "Something went wrong…"))
            }
        }
    }

}
