package com.waveneuro.ui.introduction.splash.viewmodel

import android.app.Application
import com.waveneuro.data.DataManager
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.domain.usecase.user.GetUserInfoUseCase
import com.waveneuro.ui.base.handler.error.ErrorHandler
import com.waveneuro.ui.base.handler.error.model.ApiError
import com.waveneuro.ui.base.handler.error.model.AppError
import com.waveneuro.ui.base.viewmodel.BaseAndroidViewModelImpl
import com.waveneuro.ui.introduction.splash.SplashViewEffect
import com.waveneuro.ui.introduction.splash.SplashViewEvent
import javax.inject.Inject

class SplashViewModelImpl @Inject constructor(
    app: Application,
    errorHandler: ErrorHandler,
    private val getPersonalInfoUseCase: GetUserInfoUseCase,
) : BaseAndroidViewModelImpl(app, errorHandler), SplashViewModel {

    @Inject
    lateinit var dataManager: DataManager

    override val viewEffect = SingleLiveEvent<SplashViewEffect>()

    override fun processEvent(viewEvent: SplashViewEvent) {
        when (viewEvent) {
            is SplashViewEvent.Start -> {
                if (dataManager.isLoggedIn) {
                    getPersonalInfo()
                } else {
                    viewEffect.setValue(SplashViewEffect.Login)
                }
            }
        }
    }

    private fun getPersonalInfo() {
        launchPayload(customErrorConsumer = ::getUserErrorHandler) {
            val response = getPersonalInfoUseCase.getUser()
            dataManager.saveUser(response)
            viewEffect.value = SplashViewEffect.Home
        }
    }

    private fun getUserErrorHandler(appError: AppError): Boolean = if (appError is ApiError) {
        viewEffect.value = SplashViewEffect.Login
        true
    } else { false }

}
