package com.waveneuro.ui.dashboard.account.viewmodel

import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.viewmodel.BaseViewModel
import com.waveneuro.ui.dashboard.account.AccountViewEffect
import com.waveneuro.ui.dashboard.account.AccountViewEvent

interface AccountViewModel : BaseViewModel {
    val viewEffect: SingleLiveEvent<AccountViewEffect>
    fun processEvent(viewEvent: AccountViewEvent)
}