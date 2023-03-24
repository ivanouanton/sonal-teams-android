package com.waveneuro.ui.user.mfa.viewmodel

import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.viewmodel.BaseViewModel
import com.waveneuro.ui.user.mfa.MfaViewEffect
import com.waveneuro.ui.user.mfa.MfaViewEvent

interface MfaViewModel : BaseViewModel {
    val viewEffect: SingleLiveEvent<MfaViewEffect>
    fun processEvent(viewEvent: MfaViewEvent)
}