package com.waveneuro.ui.dashboard.home.bottom_sheet.edit_client.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.waveneuro.domain.model.common.SexType
import com.waveneuro.domain.model.client.ClientRq
import com.waveneuro.domain.usecase.client.UpdateClientUseCase
import com.waveneuro.ui.base.handler.error.ErrorHandler
import com.waveneuro.ui.base.viewmodel.BaseAndroidViewModelImpl
import com.waveneuro.ui.dashboard.home.bottom_sheet.edit_client.EditClientViewState
import com.waveneuro.ui.dashboard.home.bottom_sheet.edit_client.EditClientViewState.Success
import javax.inject.Inject

class EditClientViewModelImpl @Inject constructor(
    app: Application,
    errorHandler: ErrorHandler,
    private val updateClientUseCase: UpdateClientUseCase,
) : BaseAndroidViewModelImpl(app, errorHandler), EditClientViewModel {

    override val dataEditClientLive = MutableLiveData<EditClientViewState>()

    override fun updateClient(
        id: Int,
        firstName: String,
        lastName: String,
        birthday: String?,
        email: String,
        sex: SexType
    ) {
        launchPayload {
            val request = ClientRq(firstName, lastName, birthday, email, sex)

            updateClientUseCase.updateClient(id, request)
            dataEditClientLive.postValue(Success("$firstName $lastName"))
        }
    }

}