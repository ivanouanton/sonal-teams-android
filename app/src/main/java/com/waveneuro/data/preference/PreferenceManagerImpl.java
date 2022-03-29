package com.waveneuro.data.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.waveneuro.data.model.response.user.UserInfoResponse;

public class PreferenceManagerImpl implements PreferenceManager {

    private static SharedPreferences defaultPreferences = null;
    protected static final String DEFAULT_PREF_FILE_NAME = "waven_prefs";

    private static SharedPreferences encPreferences = null;
    protected static final String ENC_PREF_FILE_NAME = "waveneuro_prefs";


    public PreferenceManagerImpl(Context context) {
        defaultPreferences = context.getSharedPreferences(DEFAULT_PREF_FILE_NAME, Context.MODE_PRIVATE);
        encPreferences = context.getSharedPreferences(ENC_PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getDefaultEditor() {
        return defaultPreferences.edit();
    }

    private SharedPreferences.Editor getEncEditor() {
        return encPreferences.edit();
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
    public void saveUser(UserInfoResponse user) {
        getDefaultEditor().putString(PreferenceKeys.FAMILY_NAME, user.getFamilyName())
                .putString(PreferenceKeys.NAME, user.getName())
                .putString(PreferenceKeys.GIVEN_NAME, user.getGivenName())
                .putString(PreferenceKeys.USERNAME, user.getUsername())
                .putString(PreferenceKeys.EMAIL, user.getEmail())
                .putString(PreferenceKeys.CUSTOM_GOAL, user.getCustomGoal())
                .putString(PreferenceKeys.GENDER, user.getGender())
                .putString(PreferenceKeys.IMAGE_THUMBNAIL_URL, user.getImageThumbnailUrl())
                .putString(PreferenceKeys.LOCATION, user.getLocation())
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
    public String getTreatmentLength() {
        return defaultPreferences.getString(PreferenceKeys.TREATMENT_LENGTH, "");
    }

    @Override
    public String getProtocolFrequency() {
        return defaultPreferences.getString(PreferenceKeys.PROTOCOL_FREQUENCY, "");
    }

    @Override
    public String getName() {
        return defaultPreferences.getString(PreferenceKeys.NAME, "");
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
}
