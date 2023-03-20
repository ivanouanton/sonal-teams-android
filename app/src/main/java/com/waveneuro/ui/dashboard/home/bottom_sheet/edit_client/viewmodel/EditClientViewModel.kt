package com.waveneuro.ui.dashboard.home.bottom_sheet.edit_client.viewmodel

import androidx.lifecycle.LiveData
import com.waveneuro.domain.model.common.SexType
import com.waveneuro.ui.base.viewmodel.BaseViewModel
import com.waveneuro.ui.dashboard.home.bottom_sheet.edit_client.EditClientViewState

interface EditClientViewModel : BaseViewModel {

    val dataEditClientLive: LiveData<EditClientViewState>
    fun updateClient(
        id: Int,
        firstName: String,
        lastName: String,
        birthday: String?,
        sex: SexType
    )

}