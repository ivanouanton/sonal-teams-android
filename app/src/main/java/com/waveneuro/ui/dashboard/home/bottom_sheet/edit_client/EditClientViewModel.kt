package com.waveneuro.ui.dashboard.home.bottom_sheet.edit_client

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asif.abase.domain.base.UseCaseCallback
import com.waveneuro.data.model.request.client.ClientRequest
import com.waveneuro.data.model.request.common.SexType
import com.waveneuro.data.model.response.client.ClientResponse
import com.waveneuro.domain.usecase.patient.UpdatePatientUseCase
import com.waveneuro.ui.dashboard.home.bottom_sheet.edit_client.EditClientViewState.Error
import com.waveneuro.ui.dashboard.home.bottom_sheet.edit_client.EditClientViewState.Success
import javax.inject.Inject

class EditClientViewModel @Inject constructor(
    private val updatePatientUseCase: UpdatePatientUseCase,
) : ViewModel() {

    val dataEditClientLive = MutableLiveData<EditClientViewState>()

    fun updateClient(
        id: Int,
        firstName: String,
        lastName: String,
        birthday: String?,
        email: String,
        sex: SexType
    ) {
        val request = ClientRequest(firstName, lastName, birthday, email, sex)

        updatePatientUseCase.execute(request, id, object : UseCaseCallback<ClientResponse> {

            override fun onSuccess(response: ClientResponse) {
                dataEditClientLive.postValue(Success)
            }

            override fun onError(throwable: Throwable) {
                dataEditClientLive.postValue(Error(throwable.message))
            }

            override fun onFinish() {}

        })
    }

}