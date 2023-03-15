package com.waveneuro.ui.dashboard.organization.viewmodel

import android.app.Application
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.domain.model.client.ClientInfo
import com.waveneuro.domain.usecase.user.GetUserInfoUseCase
import com.waveneuro.ui.base.handler.error.ErrorHandler
import com.waveneuro.ui.base.viewmodel.BaseAndroidViewModelImpl
import com.waveneuro.ui.dashboard.organization.OrganizationViewEffect
import com.waveneuro.ui.dashboard.organization.OrganizationViewEvent
import com.waveneuro.ui.model.organization.UiOrganization
import com.waveneuro.ui.model.organization.mapper.OrganizationMapperImpl
import javax.inject.Inject

class OrganizationViewModelImpl @Inject constructor(
    app: Application,
    errorHandler: ErrorHandler,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val mapper: OrganizationMapperImpl
) : BaseAndroidViewModelImpl(app, errorHandler), OrganizationViewModel {

    override val viewEffect = SingleLiveEvent<OrganizationViewEffect>()

    override fun processEvent(viewEvent: OrganizationViewEvent) {
        when (viewEvent) {
            is OrganizationViewEvent.Start -> getPersonalInfo()
            is OrganizationViewEvent.BackClicked -> {
                viewEffect.postValue(OrganizationViewEffect.BackRedirect)
            }
        }
    }

    private fun getPersonalInfo() {
        launchPayload {
            val response = getUserInfoUseCase.getUser()
            viewEffect.value = OrganizationViewEffect.Success(getOrganizations(response))
        }
    }

    private fun getOrganizations(response: ClientInfo): List<UiOrganization> =
        mapper.fromDomainToUi(response.organizations)

}