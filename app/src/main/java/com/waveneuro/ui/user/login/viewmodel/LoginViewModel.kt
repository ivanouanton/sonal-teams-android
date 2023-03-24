package com.waveneuro.ui.user.login.viewmodel

import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.viewmodel.BaseViewModel
import com.waveneuro.ui.user.login.LoginViewEffect
import com.waveneuro.ui.user.login.LoginViewEvent

interface LoginViewModel : BaseViewModel {
    val viewEffect: SingleLiveEvent<LoginViewEffect>
    fun processEvent(viewEvent: LoginViewEvent)
}