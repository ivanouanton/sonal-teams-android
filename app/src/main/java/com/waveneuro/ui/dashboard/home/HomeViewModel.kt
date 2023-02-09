package com.waveneuro.ui.dashboard.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asif.abase.domain.base.UseCaseCallback
import com.waveneuro.data.DataManager
import com.waveneuro.data.model.response.patient.PatientListResponse
import com.waveneuro.data.model.response.patient.PatientResponse
import com.waveneuro.data.model.response.protocol.ProtocolResponse
import com.waveneuro.data.model.response.user.UserInfoResponse
import com.waveneuro.domain.base.SingleLiveEvent
import com.waveneuro.domain.usecase.patient.GetPatientUseCase
import com.waveneuro.domain.usecase.patient.GetPatientsUseCase
import com.waveneuro.domain.usecase.protocol.GetLatestProtocolUseCase
import com.waveneuro.domain.usecase.user.GetPersonalInfoUseCase
import com.waveneuro.ui.dashboard.home.HomeClientsViewState.*
import com.waveneuro.ui.dashboard.home.HomeViewEvent.*
import com.waveneuro.ui.dashboard.home.adapter.model.PatientItem
import com.waveneuro.ui.dashboard.home.mapper.PatientMapperImpl
import com.waveneuro.utils.ErrorUtil
import timber.log.Timber
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getLatestProtocolUseCase: GetLatestProtocolUseCase,
    private val getPersonalInfoUseCase: GetPersonalInfoUseCase,
    private val getPatientsUseCase: GetPatientsUseCase,
    private val getPatientUseCase: GetPatientUseCase,
    private val mapper: PatientMapperImpl
) : ViewModel() {

    @Inject
    lateinit var errorUtil: ErrorUtil
    @Inject
    lateinit var dataManager: DataManager

    val deviceData = MutableLiveData<HomeDeviceViewState>()
    val userData = MutableLiveData<HomeUserViewState>()
    val protocolData = MutableLiveData<HomeProtocolViewState>()
    val clientsData = MutableLiveData<HomeClientsViewState>()
    val viewEffect = SingleLiveEvent<HomeViewEffect>()

    private var page = 1
    private var totalPages = 0
    private var filters = arrayOf<Int>()
    private var patientList = mutableListOf<PatientItem>()

    fun processEvent(viewEvent: HomeViewEvent?) {
        when (viewEvent) {
            is Start -> {
//                if (deviceData.value is HomeDeviceViewState.StartSession) {
//                    return
//                }
//                deviceData.postValue(HomeDeviceViewState.PairDevice)
//                getUserDetails()
                page = 1
                getClients(page, "", filters, true)
            }
            is NewQuery -> {
                page = 1
                getClients(page, viewEvent.query, filters, true)
            }
            is DeviceDisconnected -> {
                deviceData.postValue(HomeDeviceViewState.PairDevice)
            }
            is DeviceConnected -> {
                deviceData.postValue(HomeDeviceViewState.StartSession)
            }
            is StartSessionClicked -> {
                viewEffect.postValue(HomeViewEffect.DeviceRedirect)
            }
            else -> {}
        }
    }

    private fun getUserDetails() {
        if (dataManager.user != null
            && dataManager.user.isNameAvailable
        ) {
            userData.postValue(HomeUserViewState.Success(dataManager.user))
        } else {
            getPersonalInfo()
        }
    }

    private fun getPersonalInfo() {
        getPersonalInfoUseCase.execute(object : UseCaseCallback<Any?> {
            override fun onSuccess(response: Any?) {
                Timber.e("PROFILE_SUCCESS")
                if (response is UserInfoResponse) {
                    dataManager.saveUser(response as UserInfoResponse?)
                    userData.postValue(
                        HomeUserViewState.Success(
                            dataManager.user
                        )
                    )
                }
            }

            override fun onError(throwable: Throwable) {
                Timber.e("PROFILE_FAILURE")
            }

            override fun onFinish() {}
        })
    }

    private fun getClients(page: Int?, query: String?, filters: Array<Int>?, isNew: Boolean = false) {
        protocolData.postValue(HomeProtocolViewState.Loading(true))
        getPatientsUseCase.execute(page, query, filters,
            object : UseCaseCallback<PatientListResponse> {
                override fun onSuccess(response: PatientListResponse) {
                    Timber.e("response = $response")
                    protocolData.postValue(HomeProtocolViewState.Loading(false))
                    if (isNew) patientList.clear()
                    patientList.addAll(mapper.fromDomainToUi(response.patients))
                    clientsData.postValue(Success((patientList)))
                    totalPages = response.pages
                }

                override fun onError(throwable: Throwable) {
                    Timber.e("throwable = $throwable")
                    val error = errorUtil.parseError(throwable)
                    protocolData.postValue(HomeProtocolViewState.Loading(false))
                    clientsData.postValue(Error(error.message))
                }

                override fun onFinish() {}
        })
    }

    fun getClientWithId(id: Int) {
        protocolData.postValue(HomeProtocolViewState.Loading(true))
        getPatientUseCase.execute(id, object : UseCaseCallback<PatientResponse> {
            override fun onSuccess(response: PatientResponse) {
                val patientResponse = response as PatientResponse
                getLatestProtocolUseCase.execute(id, object : UseCaseCallback<ProtocolResponse> {
                    override fun onSuccess(protocolResponse: ProtocolResponse) {
                        dataManager.saveProtocolFrequency(protocolResponse.protocolFrequency)
                        dataManager.saveTreatmentLength(protocolResponse.treatmentLength)
                        dataManager.saveProtocolId(protocolResponse.id)
                        dataManager.saveEegId(protocolResponse.eegId)
                        dataManager.savePatientId(id.toLong())
                        protocolData.postValue(HomeProtocolViewState.Loading(false))
                        clientsData.postValue(PatientSuccess(patientResponse, true))
                    }

                    override fun onError(throwable: Throwable) {
                        protocolData.postValue(HomeProtocolViewState.Loading(false))
                        clientsData.postValue(PatientSuccess(patientResponse, false))
                    }

                    override fun onFinish() {}
                })
            }

            override fun onError(throwable: Throwable) {
                val error = errorUtil.parseError(throwable)
                protocolData.postValue(HomeProtocolViewState.Loading(false))
            }

            override fun onFinish() {}
        })
    }

    fun startSessionForClientWithId(id: Int) {
        protocolData.postValue(HomeProtocolViewState.Loading(true))
        getLatestProtocolUseCase.execute(id, object : UseCaseCallback<ProtocolResponse> {
            override fun onSuccess(protocolResponse: ProtocolResponse) {
                dataManager.saveProtocolFrequency(protocolResponse.protocolFrequency)
                dataManager.saveTreatmentLength(protocolResponse.treatmentLength)
                dataManager.saveProtocolId(protocolResponse.id)
                dataManager.saveEegId(protocolResponse.eegId)
                dataManager.savePatientId(id.toLong())
                protocolData.postValue(HomeProtocolViewState.Loading(false))
                clientsData.postValue(PatientSessionSuccess(id))
            }

            override fun onError(throwable: Throwable) {}
            override fun onFinish() {}
        })
    }

    fun getMoreClients(query: String) {
        Timber.e("page = $page\ntotalPages = $totalPages")
        if (page < totalPages) {
            page += 1
            getClients(page, query, filters)
        }
    }

    fun setNewFilters(newFilters: Array<Int>) {
        filters = newFilters
    }

}