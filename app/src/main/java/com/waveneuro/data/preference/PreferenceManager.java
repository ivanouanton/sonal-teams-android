package com.waveneuro.data.preference;

import com.waveneuro.domain.model.client.ClientInfo;

public interface PreferenceManager {

    String getAccessToken();

    void setAccessToken(String accessToken);

    String getRefreshToken();

    void setRefreshToken(String refreshToken);

    void logout();

    void saveUser(ClientInfo user);

    void saveTreatmentLength(String treatmentLength);

    void saveProtocolFrequency(String protocolFrequency);

    void saveProtocolId(String protocolId);

    void saveSonalId(String sonalId);

    String getTreatmentLength();

    String getProtocolFrequency();

    String getProtocolId();

    String getSonalId();

    String getName();

    String getUserId();

    String getImageUrl();

    String getUsername();

    String getEegId();

    void saveEegId(String eegId);

    Long getPatientId();

    void savePatientId(Long eegId);

    void rememberUsername(String username);

    void rememberPassword(String password);

    String getRememberUsername();

    String getRememberPassword();

    void removeRememberUser();

    void removeRememberPassword();

    boolean getOnboardingDisplayed();

    void setOnboardingDisplayed();

    boolean getPrecautionsDisplayed();

    void setPrecautionsDisplayed();
}
