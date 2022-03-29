package com.waveneuro.ui.user.profile.notactive

import com.waveneuro.data.model.entity.User

sealed class AccountNotActiveViewState {
    class Loading(val loading: Boolean) : AccountNotActiveViewState()
    class Success(val item: User) : AccountNotActiveViewState()
    class Failure(val error: Error) : AccountNotActiveViewState()
}