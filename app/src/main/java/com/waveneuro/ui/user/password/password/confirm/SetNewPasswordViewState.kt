package com.waveneuro.ui.user.password.password.confirm

import com.asif.abase.data.model.BaseError

sealed class SetNewPasswordViewState {
    class Loading(val loading: Boolean) : SetNewPasswordViewState()
    class Success: SetNewPasswordViewState()
    class Failure(val error: BaseError) : SetNewPasswordViewState()
}