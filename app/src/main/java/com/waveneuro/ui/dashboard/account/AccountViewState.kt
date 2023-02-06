package com.waveneuro.ui.dashboard.account

import com.asif.abase.data.model.BaseError
import com.asif.abase.data.model.BaseModel

sealed class AccountViewState {
    data class Loading(val loading: Boolean) : AccountViewState()
    data class Success(val item: BaseModel) : AccountViewState()
    data class Failure(val error: BaseError) : AccountViewState()
}