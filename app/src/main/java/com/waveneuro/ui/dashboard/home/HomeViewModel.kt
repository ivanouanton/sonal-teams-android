package com.waveneuro.ui.dashboard.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asif.abase.domain.base.UseCaseCallback
import com.waveneuro.data.DataManager
import com.waveneuro.data.model.response.client.ClientListResponse
import com.waveneuro.data.model.response.client.ClientResponse
import com.waveneuro.data.model.response.organization.OrganizationResponse
import com.waveneuro.data.model.response.protocol.ProtocolResponse
import com.waveneuro.data.model.response.user.UserInfoResponse
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.domain.usecase.patient.GetOrganizationsUseCase
import com.waveneuro.domain.usecase.patient.GetPatientUseCase
import com.waveneuro.domain.usecase.patient.GetPatientsUseCase
import com.waveneuro.domain.usecase.protocol.GetLatestProtocolUseCase
import com.waveneuro.domain.usecase.user.GetPersonalInfoUseCase
import com.waveneuro.ui.dashboard.home.HomeClientsViewState.*
import com.waveneuro.ui.dashboard.home.HomeViewEvent.*
import com.waveneuro.ui.model.client.ClientUi
import com.waveneuro.ui.model.client.mapper.ClientMapperImpl
import com.waveneuro.utils.ErrorUtil
import timber.log.Timber
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getLatestProtocolUseCase: GetLatestProtocolUseCase,
    private val getPersonalInfoUseCase: GetPersonalInfoUseCase,
    private val getPatientsUseCase: GetPatientsUseCase,
    private val getPatientUseCase: GetPatientUseCase,
    private val getOrganizationsUseCase: GetOrganizationsUseCase,
    private val mapper: ClientMapperImpl
) : ViewModel() {

    @Inject
    lateinit var errorUtil: ErrorUtil
    @Inject
    lateinit var dataManager: DataManager

    val userData = MutableLiveData<HomeUserViewState>()
    val protocolData = MutableLiveData<HomeProtocolViewState>()
    val clientsData = MutableLiveData<HomeClientsViewState>()
    val viewEffect = SingleLiveEvent<HomeViewEffect>()

    private val _filters = MutableLiveData(listOf<Int>())
    val filters: LiveData<List<Int>> = _filters

    private var page = 1
    private var totalPages = 0
    private val clientList = mutableListOf<ClientUi>()

    fun processEvent(viewEvent: HomeViewEvent?) {
        when (viewEvent) {
            is Start -> {
                getUserDetails() // TODO check
                resetPage()
                getClients(page, "", filters.value, true)
                getOrganizations()
            }
            is NewQuery -> {
                resetPage()
                getClients(page, viewEvent.query, filters.value, true)
            }
            is OnClientClick -> {
                getClient(viewEvent.id)
            }
            is ClientSuccess -> {
                getProtocol(viewEvent.client)
            }
            is StartSessionClicked -> {
                clientsData.postValue(PatientSessionSuccess())
//                viewEffect.postValue(HomeViewEffect.SessionRedirect(
//                    viewEvent.treatmentLength,
//                    viewEvent.protocolFrequency,
//                    viewEvent.sonalId
//                ))
            }
//            is DeviceDisconnected -> {
//                deviceData.postValue(HomeDeviceViewState.PairDevice)
//            }
//            is DeviceConnected -> {
//                deviceData.postValue(HomeDeviceViewState.StartSession)
//            }

            else -> {}
        }
    }

    private fun getUserDetails() {
        if (dataManager.user != null && dataManager.user.isNameAvailable) {
            userData.postValue(HomeUserViewState.Success(dataManager.user))
        } else {
            getPersonalInfo()
        }
    }

    private fun getPersonalInfo() {
        getPersonalInfoUseCase.execute(object : UseCaseCallback<UserInfoResponse> {
            override fun onSuccess(response: UserInfoResponse) {
                Timber.e("PROFILE_SUCCESS")
                dataManager.saveUser(response)
                userData.postValue(
                    HomeUserViewState.Success(
                        dataManager.user
                    )
                )
            }

            override fun onError(throwable: Throwable) {
                Timber.e("PROFILE_FAILURE")
            }

            override fun onFinish() {}
        })
    }

    private fun getClients(page: Int?, query: String?, filters: List<Int>?, isNew: Boolean = false) {
        protocolData.postValue(HomeProtocolViewState.Loading(true))
        getPatientsUseCase.execute(page, query, filters?.toTypedArray(),
            object : UseCaseCallback<ClientListResponse> {

                override fun onSuccess(response: ClientListResponse) {
                    Timber.e("response = $response")
                    totalPages = response.pages
                    if (isNew) clientList.clear()
                    clientList.addAll(mapper.fromApiToUi(response.patients))
                    clientsData.postValue(ClientsSuccess((clientList)))
                    protocolData.postValue(HomeProtocolViewState.Loading(false))
                }

                override fun onError(throwable: Throwable) {
                    Timber.e("throwable = $throwable")
                    val error = errorUtil.parseError(throwable)
                    clientsData.postValue(Error(error.message))
                    protocolData.postValue(HomeProtocolViewState.Loading(false))
                }

                override fun onFinish() {}
        })
    }

    private fun getClient(id: Int) {
        protocolData.postValue(HomeProtocolViewState.Loading(true))
        getPatientUseCase.execute(id, object : UseCaseCallback<ClientResponse> {

                override fun onSuccess(response: ClientResponse) {
                    Timber.e("response = $response")
                    processEvent(ClientSuccess(mapper.fromApiToUi(response)))
                    protocolData.postValue(HomeProtocolViewState.Loading(false))
                }

                override fun onError(throwable: Throwable) {
                    Timber.e("throwable = $throwable")
                    val error = errorUtil.parseError(throwable)
                    clientsData.postValue(Error(error.message))
                    protocolData.postValue(HomeProtocolViewState.Loading(false))
                }

                override fun onFinish() {}
            })
    }

    private fun getProtocol(client: ClientUi) {
        protocolData.postValue(HomeProtocolViewState.Loading(true))
        getLatestProtocolUseCase.execute(client.id, object : UseCaseCallback<ProtocolResponse> {
            override fun onSuccess(response: ProtocolResponse) {
                with(response) {
                    dataManager.apply {
                        saveProtocolFrequency(protocolFrequency)
                        saveTreatmentLength(treatmentLength)
                        saveProtocolId(id)
                        saveEegId(eegId)
                        savePatientId(client.id.toLong())
                    }
                }

                protocolData.postValue(HomeProtocolViewState.Loading(false))
                clientsData.postValue(ClientProtocolSuccess(client, true))
            }

            override fun onError(throwable: Throwable) {
                protocolData.postValue(HomeProtocolViewState.Loading(false))
                clientsData.postValue(ClientProtocolSuccess(client, false))
            }

            override fun onFinish() {}
        })
    }

    private fun getOrganizations() {
        protocolData.postValue(HomeProtocolViewState.Loading(true))
        getOrganizationsUseCase.execute(object: UseCaseCallback<List<OrganizationResponse>> {

            override fun onSuccess(response: List<OrganizationResponse>) {
                clientsData.postValue(OrganizationsSuccess(response))
                protocolData.postValue(HomeProtocolViewState.Loading(false))
            }

            override fun onError(throwable: Throwable) {
                protocolData.postValue(HomeProtocolViewState.Loading(false))
            }

            override fun onFinish() {}
        })
    }

    private fun resetPage() {
        page = 1
    }

    fun getMoreClients(query: String) {
        if (page < totalPages) {
            page += 1
            getClients(page, query, filters.value)
        }
    }

    fun setNewFilters(newFilters: List<Int>) {
        _filters.value = newFilters
    }

}