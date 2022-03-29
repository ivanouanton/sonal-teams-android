package com.waveneuro.ui.dashboard.account

import com.asif.abase.data.model.BaseError
import com.asif.abase.data.model.BaseModel

sealed class AccountViewState {
    class Loading(val loading: Boolean) : AccountViewState()
    class Success(val item: BaseModel) : AccountViewState()
    class Failure(val error: BaseError) : AccountViewState()
}