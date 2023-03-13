package com.waveneuro.ui.user.password.code.viewmodel

import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.viewmodel.BaseViewModel
import com.waveneuro.ui.user.password.code.ForgotPasswordCodeViewEffect
import com.waveneuro.ui.user.password.code.ForgotPasswordCodeViewEvent

interface ForgotPasswordCodeViewModel : BaseViewModel {
    val viewEffect: SingleLiveEvent<ForgotPasswordCodeViewEffect>
    fun processEvent(viewEvent: ForgotPasswordCodeViewEvent)
}