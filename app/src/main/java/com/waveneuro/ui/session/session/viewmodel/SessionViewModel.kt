package com.waveneuro.ui.session.session.viewmodel

import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.viewmodel.BaseViewModel
import com.waveneuro.ui.session.session.SessionViewEffect
import com.waveneuro.ui.session.session.SessionViewEvent

interface SessionViewModel : BaseViewModel {
    val viewEffect: SingleLiveEvent<SessionViewEffect>
    fun processEvent(viewEvent: SessionViewEvent)
}