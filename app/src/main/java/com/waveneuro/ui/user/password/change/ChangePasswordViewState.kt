package com.waveneuro.ui.user.password.change

import com.waveneuro.data.model.entity.User

sealed class ChangePasswordViewState {
    class Loading(val loading: Boolean) : ChangePasswordViewState()
    class Success(val item: User) : ChangePasswordViewState()
    class Failure(val error: Error) : ChangePasswordViewState()
}