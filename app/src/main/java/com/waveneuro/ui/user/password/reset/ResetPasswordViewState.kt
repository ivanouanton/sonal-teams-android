package com.waveneuro.ui.user.password.reset

import com.asif.abase.data.model.BaseError

sealed class ResetPasswordViewState {
    class Loading(val loading: Boolean) : ResetPasswordViewState()
    class Success() : ResetPasswordViewState()
    class Failure(val error: BaseError) : ResetPasswordViewState()
}