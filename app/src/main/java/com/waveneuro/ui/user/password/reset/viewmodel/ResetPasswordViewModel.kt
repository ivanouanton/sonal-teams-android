package com.waveneuro.ui.user.password.reset.viewmodel

import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.viewmodel.BaseViewModel
import com.waveneuro.ui.user.password.reset.ResetPasswordViewEffect
import com.waveneuro.ui.user.password.reset.ResetPasswordViewEvent

interface ResetPasswordViewModel : BaseViewModel {
    val viewEffect: SingleLiveEvent<ResetPasswordViewEffect>
    fun processEvent(viewEvent: ResetPasswordViewEvent)
}