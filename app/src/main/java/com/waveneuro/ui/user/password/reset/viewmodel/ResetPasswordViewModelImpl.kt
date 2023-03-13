package com.waveneuro.ui.user.password.reset.viewmodel

import android.app.Application
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.domain.usecase.password.ForgotPasswordUseCase
import com.waveneuro.ui.base.handler.error.ErrorHandler
import com.waveneuro.ui.base.handler.error.model.ApiError
import com.waveneuro.ui.base.handler.error.model.AppError
import com.waveneuro.ui.base.handler.error.model.ErrorName.USER_DOES_NOT_EXIST
import com.waveneuro.ui.base.utils.Event
import com.waveneuro.ui.base.viewmodel.BaseAndroidViewModelImpl
import com.waveneuro.ui.user.password.reset.ResetPasswordViewEffect
import com.waveneuro.ui.user.password.reset.ResetPasswordViewEffect.*
import com.waveneuro.ui.user.password.reset.ResetPasswordViewEvent
import com.waveneuro.ui.user.password.reset.ResetPasswordViewEvent.AboutUsClicked
import com.waveneuro.ui.user.password.reset.ResetPasswordViewEvent.ForgotUsernameClicked
import javax.inject.Inject

class ResetPasswordViewModelImpl @Inject constructor(
    app: Application,
    errorHandler: ErrorHandler,
    private val forgotPasswordUseCase: ForgotPasswordUseCase
) : BaseAndroidViewModelImpl(app, errorHandler), ResetPasswordViewModel {

    override val viewEffect = SingleLiveEvent<ResetPasswordViewEffect>()

    override fun processEvent(viewEvent: ResetPasswordViewEvent) {
        when (viewEvent) {
            is ResetPasswordViewEvent.ForgotPasswordClicked -> {
                resetPassword(viewEvent.username)
            }
            is ResetPasswordViewEvent.BackClicked -> {
                viewEffect.postValue(BackRedirect)
            }
            is ForgotUsernameClicked -> {
                viewEffect.postValue(ForgotUsernameRedirect)
            }
            is ResetPasswordViewEvent.RegisterClicked -> {
                viewEffect.postValue(RegisterRedirect)
            }
            is AboutUsClicked -> {
                viewEffect.postValue(AboutUsRedirect)
            }
            is ResetPasswordViewEvent.LoginClicked -> {
                viewEffect.postValue(LoginRedirect)
            }
        }
    }

    private fun resetPassword(username: String) {
        launchPayload(customErrorConsumer = ::forgotPasswordErrorHandler) {
            forgotPasswordUseCase.forgotPassword(email = username)
            viewEffect.postValue(Success)
        }
    }

    private fun forgotPasswordErrorHandler(appError: AppError): Boolean = if (appError is ApiError) {
        try {
            when (appError.error) {
                USER_DOES_NOT_EXIST -> {
                    message.value = Event("User not found.")
                    true
                }
                //TODO add
//                if (Integer.valueOf(error.error.code) == 400) {
//                    val snackBar =
//                        Snackbar.make(binding.rpaRoot, R.string.email_does_not_match, Snackbar.LENGTH_LONG)
//                    snackBar.duration = 100000000
//                    snackBar.setAction(R.string.ok) { snackBar.dismiss() }
//                    snackBar.show()
//                }
                else -> false
            }
        } catch (e: Exception) {
            false
        }
    } else { false }

}
