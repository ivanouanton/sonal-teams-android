package com.waveneuro.ui.dashboard.organization

import com.asif.abase.data.model.BaseError
import com.asif.abase.data.model.BaseModel

sealed class OrganizationViewState {
    data class Loading(val loading: Boolean) : OrganizationViewState()
    data class Success(val item: BaseModel) : OrganizationViewState()
    data class Failure(val error: BaseError) : OrganizationViewState()
}