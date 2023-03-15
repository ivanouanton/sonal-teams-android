package com.waveneuro.ui.session.history.viewmodel

import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.viewmodel.BaseViewModel
import com.waveneuro.ui.session.history.SessionHistoryViewEffect
import com.waveneuro.ui.session.history.SessionHistoryViewEvent

interface SessionHistoryViewModel : BaseViewModel {
    val viewEffect: SingleLiveEvent<SessionHistoryViewEffect>
    fun processEvent(viewEvent: SessionHistoryViewEvent)
}