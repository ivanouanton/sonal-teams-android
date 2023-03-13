package com.waveneuro.ui.user.password.new_password.viewmodel

import android.app.Application
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.domain.usecase.password.SetNewPasswordUseCase
import com.waveneuro.ui.base.handler.error.ErrorHandler
import com.waveneuro.ui.base.viewmodel.BaseAndroidViewModelImpl
import com.waveneuro.ui.user.password.new_password.SetNewPasswordViewEffect
import com.waveneuro.ui.user.password.new_password.SetNewPasswordViewEvent
import javax.inject.Inject

class SetNewPasswordViewModelImpl @Inject constructor(
    app: Application,
    errorHandler: ErrorHandler,
    private val setNewPasswordUseCase: SetNewPasswordUseCase
) : BaseAndroidViewModelImpl(app, errorHandler), SetNewPasswordViewModel {

    override val viewEffect = SingleLiveEvent<SetNewPasswordViewEffect>()

    override fun processEvent(viewEvent: SetNewPasswordViewEvent) {
        when (viewEvent) {
            is SetNewPasswordViewEvent.SetNewPassword -> {
                setNewPassword(viewEvent.email, viewEvent.code, viewEvent.password)
            }
        }
    }

    private fun setNewPassword(email: String, code: String, password: String) {
        launchPayload {
            //TODO save tokens
            val response = setNewPasswordUseCase.setNewPassword(email, code, password)
            viewEffect.postValue(SetNewPasswordViewEffect.Success)
        }
    }

}