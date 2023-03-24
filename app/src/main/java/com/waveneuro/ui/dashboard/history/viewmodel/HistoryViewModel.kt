package com.waveneuro.ui.dashboard.history.viewmodel

import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.viewmodel.BaseViewModel
import com.waveneuro.ui.dashboard.history.HistoryViewEffect
import com.waveneuro.ui.dashboard.history.HistoryViewEvent

interface HistoryViewModel : BaseViewModel {
    val viewEffect: SingleLiveEvent<HistoryViewEffect>
    fun processEvent(viewEvent: HistoryViewEvent)
}