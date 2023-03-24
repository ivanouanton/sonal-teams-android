package com.waveneuro.data

import com.waveneuro.data.preference.PreferenceManager
import com.waveneuro.domain.model.user.UserInfo

class DataManagerImpl(
    private val preferenceManager: PreferenceManager
) : DataManager {

    override val isLoggedIn: Boolean =
        preferenceManager.accessToken.isNullOrEmpty().not()

    override val user: UserInfo = UserInfo(
        id = preferenceManager.userId,
        firstName = preferenceManager.firstName,
        lastName = preferenceManager.lastName,
        email = preferenceManager.email,
        role = "",
        organizations = emptyList()
    )

    override val treatmentLength: String =
        preferenceManager.treatmentLength

    override val protocolFrequency: String =
        preferenceManager.protocolFrequency

    override val protocolId: String =
        preferenceManager.protocolId

    override val sonalId: String =
        preferenceManager.sonalId

    override val isOnboardingDisplayed: Boolean =
        preferenceManager.onboardingDisplayed

    override val eegId: String =
        preferenceManager.eegId

    override val patientId: Long =
        preferenceManager.patientId

    override val rememberUsername: String =
        preferenceManager.rememberUsername

    override val rememberPassword: String =
        preferenceManager.rememberPassword

    override val isPrecautionsDisplayed: Boolean =
        preferenceManager.precautionsDisplayed


    override fun saveAccessToken(accessToken: String) {
        preferenceManager.accessToken = accessToken
    }

    override fun saveRefreshToken(refreshToken: String) {
        preferenceManager.refreshToken = refreshToken
    }

    override fun logout() {
        preferenceManager.logout()
    }

    override fun saveUser(response: UserInfo) {
        preferenceManager.saveUser(response)
    }

    override fun saveTreatmentLength(treatmentLength: String) {
        preferenceManager.saveTreatmentLength(treatmentLength)
    }

    override fun saveProtocolFrequency(protocolFrequency: String) {
        preferenceManager.saveProtocolFrequency(protocolFrequency)
    }

    override fun saveProtocolId(protocolId: String) {
        preferenceManager.saveProtocolId(protocolId)
    }

    override fun saveSonalId(sonalId: String) {
        preferenceManager.saveSonalId(sonalId)
    }

    override fun setOnboardingDisplayed() {
        preferenceManager.setOnboardingDisplayed()
    }

    override fun saveEegId(eegId: String) {
        preferenceManager.saveEegId(eegId)
    }

    override fun savePatientId(patientId: Long) {
        preferenceManager.savePatientId(patientId)
    }

    override fun rememberUsername(username: String) {
        preferenceManager.rememberUsername(username)
    }

    override fun removeRememberUser() {
        preferenceManager.removeRememberUser()
    }

    override fun rememberPassword(password: String) {
        preferenceManager.rememberPassword(password)
    }

    override fun removeRememberPassword() {
        preferenceManager.removeRememberPassword()
    }

    override fun setPrecautionsDisplayed() {
        preferenceManager.setPrecautionsDisplayed()
    }

}