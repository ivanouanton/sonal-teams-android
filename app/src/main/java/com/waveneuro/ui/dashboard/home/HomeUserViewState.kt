package com.waveneuro.ui.dashboard.home

import com.waveneuro.data.model.entity.User

sealed class HomeUserViewState {
    class Success(val item: User) : HomeUserViewState()
}