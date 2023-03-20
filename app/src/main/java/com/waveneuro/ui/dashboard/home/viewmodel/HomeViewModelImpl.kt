package com.waveneuro.ui.dashboard.home.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.waveneuro.data.DataManager
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.domain.usecase.client.GetClientUseCase
import com.waveneuro.domain.usecase.client.GetClientsUseCase
import com.waveneuro.domain.usecase.protocol.GetLatestProtocolUseCase
import com.waveneuro.domain.usecase.user.GetOrganizationsUseCase
import com.waveneuro.domain.usecase.user.GetUserInfoUseCase
import com.waveneuro.ui.base.handler.error.ErrorHandler
import com.waveneuro.ui.base.handler.error.model.ApiError
import com.waveneuro.ui.base.handler.error.model.AppError
import com.waveneuro.ui.base.handler.error.model.ErrorType
import com.waveneuro.ui.base.viewmodel.BaseAndroidViewModelImpl
import com.waveneuro.ui.dashboard.home.HomeClientsViewState
import com.waveneuro.ui.dashboard.home.HomeClientsViewState.*
import com.waveneuro.ui.dashboard.home.HomeUserViewState
import com.waveneuro.ui.dashboard.home.HomeViewEffect
import com.waveneuro.ui.dashboard.home.HomeViewEvent
import com.waveneuro.ui.dashboard.home.HomeViewEvent.*
import com.waveneuro.ui.model.client.ClientUi
import com.waveneuro.ui.model.client.mapper.ClientMapperImpl
import timber.log.Timber
import javax.inject.Inject

class HomeViewModelImpl @Inject constructor(
    app: Application,
    errorHandler: ErrorHandler,
    private val getLatestProtocolUseCase: GetLatestProtocolUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getClientsUseCase: GetClientsUseCase,
    private val getClientUseCase: GetClientUseCase,
    private val getOrganizationsUseCase: GetOrganizationsUseCase,
    private val mapper: ClientMapperImpl
) : BaseAndroidViewModelImpl(app, errorHandler), HomeViewModel {

    @Inject
    lateinit var dataManager: DataManager

    override val viewEffect = SingleLiveEvent<HomeViewEffect>()
    override val filters = MutableLiveData(listOf<Int>())

    val userData = MutableLiveData<HomeUserViewState>()
    val clientsData = MutableLiveData<HomeClientsViewState>()

    private var page = 1
    private var totalPages = 0
    private val clientList = mutableListOf<ClientUi>()

    private var currentClient: ClientUi? = null

    override fun processEvent(viewEvent: HomeViewEvent) {
        when (viewEvent) {
            is Start -> {
                getUserDetails()
                resetPage()
                getClients(page, "", filters.value ?: emptyList(), true)
                getOrganizations()
            }
            is NewQuery -> {
                resetPage()
                getClients(page, viewEvent.query, filters.value ?: emptyList(), true)
            }
            is OnClientClick -> {
                getClient(viewEvent.id)
            }
            is ClientSuccess -> {
                getProtocol(viewEvent.client)
            }
            is OnStartSessionClick -> {
                viewEffect.postValue(HomeViewEffect.DeviceRedirect)
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
        if (dataManager.user != null) {
            userData.postValue(HomeUserViewState.Success(dataManager.user))
            Timber.e("dataManager.user = ${dataManager.user}")
        } else {
            getUserInfo()
        }
    }

    private fun getUserInfo() {
        launchPayload {
            val response = getUserInfoUseCase.getUser()
            dataManager.saveUser(response)
            userData.postValue(HomeUserViewState.Success(
                dataManager.user
            ))
        }
    }

    private fun getClients(page: Int, query: String, filters: List<Int>, isNew: Boolean = false) {
        launchPayload {
            val response = getClientsUseCase.getClients(page, query, filters)

            totalPages = response.pages
            if (isNew) clientList.clear()
            clientList.addAll(mapper.fromDomainToUi(response.patients))
            clientsData.postValue(ClientsSuccess((clientList)))
        }
    }

    private fun getOrganizations() {
        launchPayload {
            val response = getOrganizationsUseCase.getOrganizations()

            clientsData.postValue(OrganizationsSuccess(response))
        }
    }

    private fun getClient(id: Int) {
        launchPayload {
            val response = getClientUseCase.getClient(id)

            processEvent(ClientSuccess(mapper.fromDomainToUi(response)))
        }
    }

    private fun getProtocol(client: ClientUi) {
        launchPayload(customErrorConsumer = ::protocolErrorHandler) {
            currentClient = client

            val response = getLatestProtocolUseCase.getProtocol(client.id)

            with(response) {
                dataManager.saveProtocolFrequency(protocolFrequency)
                dataManager.saveTreatmentLength(treatmentLength)
                dataManager.saveProtocolId(id)
                dataManager.saveEegId(eegId)
                dataManager.savePatientId(client.id.toLong())
            }

            clientsData.postValue(ClientProtocolSuccess(client, true))
        }
    }

    private fun protocolErrorHandler(appError: AppError): Boolean = if (appError is ApiError) {
        try {
            currentClient?.let {
                clientsData.postValue(ClientProtocolSuccess(it, false))
            }
            when (appError.errorType) {
                ErrorType.TOS_DOES_NOT_SIGNED -> {
                    true
                }
                ErrorType.SESSION_ARE_NOT_AVAILABLE -> {
                    true
                }
                else -> false
            }
        } catch (e: Exception) { false }
    } else { false }

    private fun resetPage() {
        page = 1
    }

    fun getMoreClients(query: String) {
        if (page < totalPages) {
            page += 1
            getClients(page, query, filters.value ?: emptyList())
        }
    }

}