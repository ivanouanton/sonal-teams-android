package com.waveneuro.data.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.waveneuro.domain.model.client.ClientInfo;

public class PreferenceManagerImpl implements PreferenceManager {

    private SharedPreferences defaultPreferences;
    protected static final String DEFAULT_PREF_FILE_NAME = "waven_prefs";

    private SharedPreferences nonUserPreferences;
    protected static final String NON_USER_RELATED_PREF_FILE_NAME = "non_user_prefs";

    private SharedPreferences encPreferences;
    protected static final String ENC_PREF_FILE_NAME = "waveneuro_prefs";


    public PreferenceManagerImpl(Context context) {
        defaultPreferences = context.getSharedPreferences(DEFAULT_PREF_FILE_NAME, Context.MODE_PRIVATE);
        encPreferences = context.getSharedPreferences(ENC_PREF_FILE_NAME, Context.MODE_PRIVATE);
        nonUserPreferences = context.getSharedPreferences(NON_USER_RELATED_PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getDefaultEditor() {
        return defaultPreferences.edit();
    }

    private SharedPreferences.Editor getEncEditor() {
        return encPreferences.edit();
    }

    private SharedPreferences.Editor getNonUserEditor() {
        return nonUserPreferences.edit();
    }

    @Override
    public String getAccessToken() {
        return defaultPreferences.getString(PreferenceKeys.ACCESS_TOKEN, null);
    }

    @Override
    public void setAccessToken(String accessToken) {
        getDefaultEditor().putString(PreferenceKeys.ACCESS_TOKEN, accessToken).apply();
    }

    @Override
    public String getRefreshToken() {
        return defaultPreferences.getString(PreferenceKeys.REFRESH_TOKEN, null);
    }

    @Override
    public void setRefreshToken(String refreshToken) {
        getDefaultEditor().putString(PreferenceKeys.REFRESH_TOKEN, refreshToken).apply();
    }

    @Override
    public void logout() {
        getDefaultEditor().clear().commit();
    }

    @Override
    public void saveUser(ClientInfo user) {
        getDefaultEditor().putString(PreferenceKeys.USER_ID, user.getId())
                .putString(PreferenceKeys.NAME, user.getFirstName())
                .putString(PreferenceKeys.GIVEN_NAME, user.getLastName())
                .putString(PreferenceKeys.EMAIL, user.getEmail())
                .commit();
        defaultPreferences.getAll();
    }

    @Override
    public void saveTreatmentLength(String treatmentLength) {
        getDefaultEditor().putString(PreferenceKeys.TREATMENT_LENGTH, treatmentLength).commit();
    }

    @Override
    public void saveProtocolFrequency(String protocolFrequency) {
        getDefaultEditor().putString(PreferenceKeys.PROTOCOL_FREQUENCY, protocolFrequency).commit();
    }

    @Override
    public void saveProtocolId(String protocolId) {
        getDefaultEditor().putString(PreferenceKeys.PROTOCOL_ID, protocolId).commit();
    }

    @Override
    public void saveSonalId(String sonalId) {
        getDefaultEditor().putString(PreferenceKeys.SONAL_ID, sonalId).commit();
    }

    @Override
    public String getTreatmentLength() {
        return defaultPreferences.getString(PreferenceKeys.TREATMENT_LENGTH, "");
    }

    @Override
    public String getProtocolFrequency() {
        return defaultPreferences.getString(PreferenceKeys.PROTOCOL_FREQUENCY, "");
    }

    @Override
    public String getProtocolId() {
        return defaultPreferences.getString(PreferenceKeys.PROTOCOL_ID, "");
    }

    @Override
    public String getSonalId() {
        return defaultPreferences.getString(PreferenceKeys.SONAL_ID, "");
    }

    @Override
    public String getName() {
        return defaultPreferences.getString(PreferenceKeys.NAME, "");
    }

    @Override
    public String getUserId() {
        return defaultPreferences.getString(PreferenceKeys.USER_ID, "");
    }

    @Override
    public String getImageUrl() {
        return defaultPreferences.getString(PreferenceKeys.IMAGE_THUMBNAIL_URL, "");
    }

    @Override
    public String getUsername() {
        return defaultPreferences.getString(PreferenceKeys.USERNAME, "");
    }

    @Override
    public String getEegId() {
        return defaultPreferences.getString(PreferenceKeys.EEG_ID, "");
    }

    @Override
    public void saveEegId(String eegId) {
        getDefaultEditor().putString(PreferenceKeys.EEG_ID, eegId).commit();
    }

    @Override
    public Long getPatientId() {
        return defaultPreferences.getLong(PreferenceKeys.PATIENT_ID, 0L);
    }

    @Override
    public void savePatientId(Long patientId) {
        getDefaultEditor().putLong(PreferenceKeys.PATIENT_ID, patientId).commit();
    }

    @Override
    public void rememberUsername(String username) {
        getEncEditor().putString(PreferenceKeys.REMEMBER_USERNAME, username).commit();
    }

    @Override
    public void rememberPassword(String password) {
        getEncEditor().putString(PreferenceKeys.REMEMBER_PASSWORD, password).commit();
    }

    @Override
    public String getRememberUsername() {
        return encPreferences.getString(PreferenceKeys.REMEMBER_USERNAME, "");
    }

    @Override
    public String getRememberPassword() {
        return encPreferences.getString(PreferenceKeys.REMEMBER_PASSWORD, "");
    }

    @Override
    public void removeRememberUser() {
        getEncEditor().remove(PreferenceKeys.REMEMBER_USERNAME).commit();
    }

    @Override
    public void removeRememberPassword() {
        getEncEditor().remove(PreferenceKeys.REMEMBER_PASSWORD).commit();
    }

    @Override
    public boolean getOnboardingDisplayed() {
        return nonUserPreferences.getBoolean(PreferenceKeys.ONBOARDING_DISPLAYED, false);
    }

    @Override
    public void setOnboardingDisplayed() {
        getNonUserEditor().putBoolean(PreferenceKeys.ONBOARDING_DISPLAYED, true).commit();
    }

    @Override
    public boolean getPrecautionsDisplayed() {
        return nonUserPreferences.getBoolean(PreferenceKeys.PRECAUTIONS_DISPLAYED, false);
    }

    @Override
    public void setPrecautionsDisplayed() {
        getNonUserEditor().putBoolean(PreferenceKeys.PRECAUTIONS_DISPLAYED, true).commit();
    }
}
