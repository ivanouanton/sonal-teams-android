package com.waveneuro.ui.user.registration.viewmodel

import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.viewmodel.BaseViewModel
import com.waveneuro.ui.user.registration.RegistrationViewEffect
import com.waveneuro.ui.user.registration.RegistrationViewEvent

interface RegistrationViewModel : BaseViewModel {
    val viewEffect: SingleLiveEvent<RegistrationViewEffect>
    fun processEvent(viewEvent: RegistrationViewEvent)
}