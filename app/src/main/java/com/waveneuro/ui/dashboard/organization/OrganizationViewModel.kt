package com.waveneuro.ui.dashboard.organization

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asif.abase.domain.base.UseCaseCallback
import com.waveneuro.data.model.response.user.UserInfoResponse
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.domain.usecase.user.GetPersonalInfoUseCase
import com.waveneuro.ui.model.organization.OrganizationUi
import com.waveneuro.ui.model.organization.mapper.OrganizationMapperImpl
import com.waveneuro.utils.ErrorUtil
import javax.inject.Inject

class OrganizationViewModel @Inject constructor(
    private val getPersonalInfoUseCase: GetPersonalInfoUseCase,
    private val mapper: OrganizationMapperImpl
) : ViewModel() {

    @Inject
    lateinit var errorUtil: ErrorUtil

    val data = MutableLiveData<OrganizationViewState>()
    val viewEffect = SingleLiveEvent<OrganizationViewEffect>()

    fun processEvent(viewEvent: OrganizationViewEvent?) {
        when (viewEvent) {
            is OrganizationViewEvent.Start -> getPersonalInfo()
            is OrganizationViewEvent.BackClicked -> {
                viewEffect.postValue(OrganizationViewEffect.BackRedirect)
            }
            else -> {}
        }
    }

    fun getOrganizations(response: UserInfoResponse): List<OrganizationUi> =
        mapper.fromApiToUi(response.organizations)

    private fun getPersonalInfo() {
        data.postValue(OrganizationViewState.Loading(true))
        getPersonalInfoUseCase.execute(object : UseCaseCallback<UserInfoResponse> {
            override fun onSuccess(response: UserInfoResponse) {
                data.postValue(OrganizationViewState.Loading(false))
                data.value = OrganizationViewState.Success(response)
            }

            override fun onError(throwable: Throwable) {
                data.postValue(OrganizationViewState.Loading(false))
                val error = errorUtil.parseError(throwable)
                data.postValue(OrganizationViewState.Failure(error))
            }

            override fun onFinish() {}
        })
    }

}