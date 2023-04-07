package com.waveneuro.data.preference

import com.waveneuro.domain.model.user.UserInfo

interface PreferenceManager {
    var accessToken: String?
    var refreshToken: String?
    fun logout()
    fun saveUser(user: UserInfo)
    val user: UserInfo
    var treatmentLength: String?
    var protocolFrequency: String?
    var protocolId: String?
    var sonalId: String?
    var userId: String?
    var firstName: String?
    var lastName: String?
    var email: String?
    var username: String?
    var eegId: String?
    var patientId: Long
    var rememberUsername: String?
    var rememberPassword: String?
    fun removeRememberUser()
    fun removeRememberPassword()
    var isOnboardingDisplayed: Boolean
    var isPrecautionsDisplayed: Boolean
}
