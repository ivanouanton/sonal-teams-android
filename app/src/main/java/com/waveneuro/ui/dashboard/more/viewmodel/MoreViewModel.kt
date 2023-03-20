package com.waveneuro.ui.dashboard.more.viewmodel

import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.viewmodel.BaseViewModel
import com.waveneuro.ui.dashboard.more.MoreViewEffect
import com.waveneuro.ui.dashboard.more.MoreViewEvent

interface MoreViewModel : BaseViewModel {
    val viewEffect: SingleLiveEvent<MoreViewEffect>
    fun processEvent(viewEvent: MoreViewEvent)
}