package com.waveneuro.ui.dashboard.organization.viewmodel

import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.ui.base.viewmodel.BaseViewModel
import com.waveneuro.ui.dashboard.organization.OrganizationViewEffect
import com.waveneuro.ui.dashboard.organization.OrganizationViewEvent

interface OrganizationViewModel : BaseViewModel {
    val viewEffect: SingleLiveEvent<OrganizationViewEffect>
    fun processEvent(viewEvent: OrganizationViewEvent)
}