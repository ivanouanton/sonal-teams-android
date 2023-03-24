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