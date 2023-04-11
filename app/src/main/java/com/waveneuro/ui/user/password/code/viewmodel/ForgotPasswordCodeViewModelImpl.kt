package com.waveneuro.ui.user.password.code.viewmodel

import android.app.Application
import com.waveneuro.data.preference.PreferenceManagerImpl
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

    private val prefs = PreferenceManagerImpl(appCtx)

    override val viewEffect = SingleLiveEvent<ForgotPasswordCodeViewEffect>()

    override fun processEvent(viewEvent: ForgotPasswordCodeViewEvent) {
        when (viewEvent) {
            is Start -> {
                isRememberDataExist()
            }
            is LoginClicked -> {
                login(viewEvent.username, viewEvent.password)
            }
            is RememberUser -> {
                saveUserLoginDetails(viewEvent.username)
            }
            is ClearRememberUser -> {
                removeRememberUserData()
            }
            is ResetPassword -> {
                resetPassword(viewEvent.username)
            }
        }
    }

    private fun resetPassword(username: String?) {
        launchPayload {
            username?.let { forgotPasswordUseCase.forgotPassword(it) }
        }
    }

    private fun removeRememberUserData() {
        if (prefs.rememberUsername.isNullOrBlank().not()) {
            prefs.removeRememberUser()
        }
        if (prefs.rememberPassword.isNullOrBlank().not()) {
            prefs.removeRememberPassword()
        }
    }

    private fun isRememberDataExist() {
        if (prefs.rememberUsername.isNullOrBlank().not()) {
            viewEffect.postValue(
                ForgotPasswordCodeViewEffect.RememberMe(prefs.rememberUsername ?: "")
            )
        }
    }

    private fun saveUserLoginDetails(username: String) {
        prefs.rememberUsername = username
    }

    private fun login(username: String, password: String) {
        launchPayload {
            val response = loginUseCase.login(username, password)
            viewEffect.postValue(ForgotPasswordCodeViewEffect.EnterMfaCode(response.session))
        }
    }

}