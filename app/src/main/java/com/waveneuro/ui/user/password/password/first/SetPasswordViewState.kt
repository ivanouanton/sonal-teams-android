package com.waveneuro.ui.user.password.password.first

import com.asif.abase.data.model.BaseError

sealed class SetPasswordViewState {
    class Loading(val loading: Boolean) : SetPasswordViewState()
    class Success : SetPasswordViewState()
    class Failure(val error: BaseError) : SetPasswordViewState()
}