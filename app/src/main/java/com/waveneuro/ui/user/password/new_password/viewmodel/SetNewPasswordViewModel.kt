package com.waveneuro.ui.user.password.new_password.viewmodel

import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.viewmodel.BaseViewModel
import com.waveneuro.ui.user.password.new_password.SetNewPasswordViewEffect
import com.waveneuro.ui.user.password.new_password.SetNewPasswordViewEvent

interface SetNewPasswordViewModel : BaseViewModel {
    val viewEffect: SingleLiveEvent<SetNewPasswordViewEffect>
    fun processEvent(viewEvent: SetNewPasswordViewEvent)
}