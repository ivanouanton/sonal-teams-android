package com.waveneuro.data

import com.waveneuro.domain.model.user.UserInfo

interface DataManager {

    val isLoggedIn: Boolean
    val user: UserInfo
    val treatmentLength: String
    val protocolFrequency: String
    val protocolId: String
    val sonalId: String
    val isOnboardingDisplayed: Boolean
    val eegId: String
    val patientId: Long
    val rememberUsername: String
    val rememberPassword: String
    val isPrecautionsDisplayed: Boolean

    fun saveAccessToken(accessToken: String)
    fun saveRefreshToken(refreshToken: String)
    fun logout()
    fun saveUser(response: UserInfo)
    fun saveTreatmentLength(treatmentLength: String)
    fun saveProtocolFrequency(protocolFrequency: String)
    fun saveProtocolId(protocolId: String)
    fun saveSonalId(sonalId: String)
    fun setOnboardingDisplayed()
    fun saveEegId(eegId: String)
    fun savePatientId(patientId: Long)
    fun rememberUsername(username: String)
    fun removeRememberUser()
    fun rememberPassword(password: String)
    fun removeRememberPassword()
    fun setPrecautionsDisplayed()

}