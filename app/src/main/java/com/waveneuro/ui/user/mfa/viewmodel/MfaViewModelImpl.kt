package com.waveneuro.ui.user.mfa.viewmodel

import android.app.Application
import com.waveneuro.data.preference.PreferenceManager
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.domain.usecase.login.ConfirmTokenUseCase
import com.waveneuro.ui.base.handler.error.ErrorHandler
import com.waveneuro.ui.base.handler.error.model.ApiError
import com.waveneuro.ui.base.handler.error.model.AppError
import com.waveneuro.ui.base.viewmodel.BaseAndroidViewModelImpl
import com.waveneuro.ui.user.mfa.MfaViewEffect
import com.waveneuro.ui.user.mfa.MfaViewEvent
import javax.inject.Inject

class MfaViewModelImpl @Inject constructor(
    app: Application,
    errorHandler: ErrorHandler,
    private val confirmTokenUseCase: ConfirmTokenUseCase
) : BaseAndroidViewModelImpl(app, errorHandler), MfaViewModel {

    @Inject
    lateinit var preferenceManager: PreferenceManager

    override val viewEffect = SingleLiveEvent<MfaViewEffect>()

    override fun processEvent(viewEvent: MfaViewEvent) {
        when(viewEvent) {
            is MfaViewEvent.ConfirmToken -> {
                confirmToken(viewEvent.mfaCode, viewEvent.username, viewEvent.session)
            }
        }
    }

    private fun confirmToken(mfaCode: String?, username: String?, session: String?) {
        launchPayload(customErrorConsumer = ::confirmTokenErrorHandler) {
            val response = confirmTokenUseCase.confirmToken(
                username ?: "",
                mfaCode ?: "",
                session ?: ""
            )
            //TODO save refresh token
            preferenceManager.accessToken = response.accessToken
            viewEffect.postValue(MfaViewEffect.Home)
        }
    }

    private fun confirmTokenErrorHandler(appError: AppError): Boolean = if (appError is ApiError) {
        viewEffect.postValue(MfaViewEffect.WrongMfaCode)
        true
    } else { false }

}