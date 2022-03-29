package com.waveneuro.ui.dashboard.account

sealed class AccountViewEvent {
    class Start : AccountViewEvent()
    data class UpdatedUser(val firstName: String, val lastName: String,
    val username: String, val email: String, val dob: String) : AccountViewEvent()
    class BackClicked : AccountViewEvent()
}