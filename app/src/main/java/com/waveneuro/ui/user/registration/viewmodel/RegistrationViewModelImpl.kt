package com.waveneuro.ui.user.registration.viewmodel

import android.app.Application
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.handler.error.ErrorHandler
import com.waveneuro.ui.base.viewmodel.BaseAndroidViewModelImpl
import com.waveneuro.ui.user.registration.RegistrationViewEffect
import com.waveneuro.ui.user.registration.RegistrationViewEvent
import javax.inject.Inject

class RegistrationViewModelImpl @Inject constructor(
    app: Application,
    errorHandler: ErrorHandler,
) : BaseAndroidViewModelImpl(app, errorHandler), RegistrationViewModel {


    override val viewEffect = SingleLiveEvent<RegistrationViewEffect>()

    override fun processEvent(viewEvent: RegistrationViewEvent) {
        when (viewEvent) {
            is RegistrationViewEvent.Start -> {}
            is RegistrationViewEvent.BackClicked -> {
                viewEffect.postValue(RegistrationViewEffect.Back)
            }
        }
    }

}