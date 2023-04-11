package com.waveneuro.data.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.waveneuro.domain.model.user.UserInfo

class PreferenceManagerImpl(context: Context) : PreferenceManager {

    private val defaultPreferences: SharedPreferences =
        context.getSharedPreferences(DEFAULT_PREF_FILE_NAME, Context.MODE_PRIVATE)
    private val nonUserPreferences: SharedPreferences =
        context.getSharedPreferences(NON_USER_RELATED_PREF_FILE_NAME, Context.MODE_PRIVATE)
    private val encPreferences: SharedPreferences =
        context.getSharedPreferences(ENC_PREF_FILE_NAME, Context.MODE_PRIVATE)

    private val defaultEditor: SharedPreferences.Editor
        get() = defaultPreferences.edit()
    private val encEditor: SharedPreferences.Editor
        get() = encPreferences.edit()


    override var accessToken: String?
        get() = defaultPreferences.getString(PreferenceKeys.ACCESS_TOKEN, null)
        set(value) = defaultPreferences.edit { putString(PreferenceKeys.ACCESS_TOKEN, value) }

    override var refreshToken: String?
        get() = defaultPreferences.getString(PreferenceKeys.REFRESH_TOKEN, null)
        set(value) = defaultPreferences.edit { putString(PreferenceKeys.REFRESH_TOKEN, value) }

    override fun logout() {
        defaultEditor.clear().commit()
    }

    override fun saveUser(user: UserInfo) {
        defaultEditor.putString(PreferenceKeys.USER_ID, user.id)
            .putString(PreferenceKeys.NAME, user.firstName)
            .putString(PreferenceKeys.GIVEN_NAME, user.lastName)
            .putString(PreferenceKeys.EMAIL, user.email)
            .commit()
        defaultPreferences.all
    }

    override val user: UserInfo = UserInfo(
        id = userId ?: "",
        firstName = firstName ?: "",
        lastName = lastName ?: "",
        email = email ?: "",
        role = "",
        organizations = emptyList()
    )

    override var treatmentLength: String?
        get() = defaultPreferences.getString(PreferenceKeys.TREATMENT_LENGTH, null)
        set(value) = defaultPreferences.edit { putString(PreferenceKeys.TREATMENT_LENGTH, value) }

    override var protocolFrequency: String?
        get() = defaultPreferences.getString(PreferenceKeys.PROTOCOL_FREQUENCY, null)
        set(value) = defaultPreferences.edit { putString(PreferenceKeys.PROTOCOL_FREQUENCY, value) }

    override var protocolId: String?
        get() = defaultPreferences.getString(PreferenceKeys.PROTOCOL_ID, null)
        set(value) = defaultPreferences.edit { putString(PreferenceKeys.PROTOCOL_ID, value) }

    override var sonalId: String?
        get() = defaultPreferences.getString(PreferenceKeys.SONAL_ID, "")
        set(value) = defaultPreferences.edit { putString(PreferenceKeys.SONAL_ID, value) }

    override var firstName: String?
        get() = defaultPreferences.getString(PreferenceKeys.NAME, null)
        set(value) = defaultPreferences.edit { putString(PreferenceKeys.NAME, value) }

    override var lastName: String?
        get() = defaultPreferences.getString(PreferenceKeys.GIVEN_NAME, null)
        set(value) = defaultPreferences.edit { putString(PreferenceKeys.GIVEN_NAME, value) }

    override var userId: String?
        get() = defaultPreferences.getString(PreferenceKeys.USER_ID, null)
        set(value) = defaultPreferences.edit { putString(PreferenceKeys.USER_ID, value) }

    override var email: String?
        get() = defaultPreferences.getString(PreferenceKeys.EMAIL, null)
        set(value) = defaultPreferences.edit { putString(PreferenceKeys.EMAIL, value) }

    override var username: String?
        get() = defaultPreferences.getString(PreferenceKeys.USERNAME, null)
        set(value) = defaultPreferences.edit { putString(PreferenceKeys.USERNAME, value) }

    override var eegId: String?
        get() = defaultPreferences.getString(PreferenceKeys.EEG_ID, null)
        set(value) = defaultPreferences.edit { putString(PreferenceKeys.EEG_ID, value) }

    override var patientId: Long
        get() = defaultPreferences.getLong(PreferenceKeys.PATIENT_ID, 0L)
        set(value) = defaultPreferences.edit { putLong(PreferenceKeys.PATIENT_ID, value) }

    override var rememberUsername: String?
        get() = encPreferences.getString(PreferenceKeys.REMEMBER_USERNAME, null)
        set(value) = encPreferences.edit { putString(PreferenceKeys.REMEMBER_USERNAME, value) }

    override var rememberPassword: String?
        get() = encPreferences.getString(PreferenceKeys.REMEMBER_PASSWORD, null)
        set(value) = encPreferences.edit { putString(PreferenceKeys.REMEMBER_PASSWORD, value) }

    override fun removeRememberUser() {
        encEditor.remove(PreferenceKeys.REMEMBER_USERNAME).commit()
    }

    override fun removeRememberPassword() {
        encEditor.remove(PreferenceKeys.REMEMBER_PASSWORD).commit()
    }

    override var isOnboardingDisplayed: Boolean
        get() = nonUserPreferences.getBoolean(PreferenceKeys.ONBOARDING_DISPLAYED, false)
        set(value) = nonUserPreferences.edit { putBoolean(PreferenceKeys.ONBOARDING_DISPLAYED, value) }

    override var isPrecautionsDisplayed: Boolean
        get() = nonUserPreferences.getBoolean(PreferenceKeys.PRECAUTIONS_DISPLAYED, false)
        set(value) = nonUserPreferences.edit { putBoolean(PreferenceKeys.PRECAUTIONS_DISPLAYED, value) }

    companion object {
        private const val DEFAULT_PREF_FILE_NAME = "waven_prefs"
        private const val NON_USER_RELATED_PREF_FILE_NAME = "non_user_prefs"
        private const val ENC_PREF_FILE_NAME = "waveneuro_prefs"
    }

}
