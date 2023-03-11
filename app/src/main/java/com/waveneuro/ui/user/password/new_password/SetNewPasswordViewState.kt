package com.waveneuro.ui.user.password.new_password

import com.asif.abase.data.model.BaseError

sealed class SetNewPasswordViewState {
    class Loading(val loading: Boolean) : SetNewPasswordViewState()
    class Success: SetNewPasswordViewState()
    class Failure(val error: BaseError) : SetNewPasswordViewState()
}