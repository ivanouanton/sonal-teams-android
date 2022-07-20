package com.waveneuro.data.preference;

import com.waveneuro.data.model.response.user.UserInfoResponse;

public interface PreferenceManager {

    String getAccessToken();

    void setAccessToken(String accessToken);

    String getRefreshToken();

    void setRefreshToken(String refreshToken);

    void logout();

    void saveUser(UserInfoResponse user);

    void saveTreatmentLength(String treatmentLength);

    void saveProtocolFrequency(String protocolFrequency);

    void saveProtocolId(String protocolId);

    void saveSonalId(String sonalId);

    String getTreatmentLength();

    String getProtocolFrequency();

    String getProtocolId();

    String getSonalId();

    String getName();

    String getImageUrl();

    String getUsername();

    String getEegId();

    void saveEegId(String eegId);

    void rememberUsername(String username);

    void rememberPassword(String password);

    String getRememberUsername();

    String getRememberPassword();

    void removeRememberUser();

    void removeRememberPassword();
}
