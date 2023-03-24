package com.waveneuro.ui.dashboard.home

import com.waveneuro.domain.model.user.UserInfo

sealed class HomeUserViewState {
    class Success(val item: UserInfo) : HomeUserViewState()
}