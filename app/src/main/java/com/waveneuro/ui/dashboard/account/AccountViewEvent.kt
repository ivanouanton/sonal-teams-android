package com.waveneuro.ui.dashboard.account

sealed class AccountViewEvent {
    object Start : AccountViewEvent()
    data class UpdatedUser(val firstName: String, val lastName: String) : AccountViewEvent()
    object BackClicked : AccountViewEvent()
}