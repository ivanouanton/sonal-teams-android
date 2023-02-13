package com.waveneuro.data;

import com.waveneuro.data.model.entity.User;
import com.waveneuro.data.model.request.account.update.AccountUpdateRequest;
import com.waveneuro.data.model.request.email.forgot.ForgotUsernameRequest;
import com.waveneuro.data.model.request.login.ConfirmTokenRequest;
import com.waveneuro.data.model.request.login.LoginRequest;
import com.waveneuro.data.model.request.password.ResetPasswordRequest;
import com.waveneuro.data.model.request.password.confirm.ForgotPasswordConfirmRequest;
import com.waveneuro.data.model.request.password.password.ForgotPasswordRequest;
import com.waveneuro.data.model.request.password.password.SetNewPasswordRequest;
import com.waveneuro.data.model.request.password.password.SetPasswordRequest;
import com.waveneuro.data.model.request.client.ClientRequest;
import com.waveneuro.data.model.request.treatment.AddTreatmentRequest;
import com.waveneuro.data.model.response.device.SonalDevicesResponse;
import com.waveneuro.data.model.response.email.forgot.ForgotUsernameResponse;
import com.waveneuro.data.model.response.login.ConfirmTokenResponse;
import com.waveneuro.data.model.response.login.LoginResponseMfa;
import com.waveneuro.data.model.response.organization.OrganizationResponse;
import com.waveneuro.data.model.response.password.ResetPasswordResponse;
import com.waveneuro.data.model.response.password.confirm.ForgotPasswordConfirmResponse;
import com.waveneuro.data.model.response.password.password.ForgotPasswordResponse;
import com.waveneuro.data.model.response.password.password.SetNewPasswordResponse;
import com.waveneuro.data.model.response.password.password.SetPasswordResponse;
import com.waveneuro.data.model.response.client.ClientListResponse;
import com.waveneuro.data.model.response.client.ClientResponse;
import com.waveneuro.data.model.response.protocol.ProtocolResponse;
import com.waveneuro.data.model.response.session.SessionResponse;
import com.waveneuro.data.model.response.treatment.TreatmentResponse;
import com.waveneuro.data.model.response.user.RefreshResponse;
import com.waveneuro.data.model.response.user.UserInfoResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface DataManager {
    Observable<LoginResponseMfa> login(LoginRequest request);

    Observable<ConfirmTokenResponse> confirmToken(ConfirmTokenRequest request);

    void saveAccessToken(String accessToken);

    void saveRefreshToken(String refreshToken);

    boolean isLoggedIn();

    Observable<ForgotUsernameResponse> forgotUsername(ForgotUsernameRequest request);

    Observable<ResetPasswordResponse> resetPassword(ResetPasswordRequest request);

    Observable<UserInfoResponse> getPersonalInfo();

    Observable<ProtocolResponse> protocol(int id);

    Observable<ClientListResponse> patients(Integer page, String startsWith, Integer[] ids);

    Observable<List<OrganizationResponse>> organizations();

    Observable<ClientResponse> patientWithId(int id);

    Observable<ClientResponse> updatePatientWithId(int id, ClientRequest request);

    Observable<ForgotPasswordResponse> forgotPassword(ForgotPasswordRequest request);

    Observable<ForgotPasswordConfirmResponse> forgotPasswordConfirm(ForgotPasswordConfirmRequest request);

    Observable<TreatmentResponse> addTreatment(AddTreatmentRequest request);

    void logout();

    Observable<RefreshResponse> refreshToken();

    void saveUser(UserInfoResponse response);

    void saveTreatmentLength(String treatmentLength);

    void saveProtocolFrequency(String protocolFrequency);

    void saveProtocolId(String protocolId);

    void saveSonalId(String sonalId);

    String getTreatmentLength();

    String getProtocolFrequency();

    User getUser();

    String getEegId();

    void saveEegId(String eegId);

    Long getPatientId();

    void savePatientId(Long patientId);

    Observable<SetNewPasswordResponse> setNewPassword(SetNewPasswordRequest request);

    Observable<SetPasswordResponse> setPassword(SetPasswordRequest request);

    void rememberUsername(String username);

    void rememberPassword(String password);

    String getRememberUsername();

    String getRememberPassword();

    void removeRememberUser();

    void removeRememberPassword();

    Observable<UserInfoResponse> updateUser(AccountUpdateRequest request);

    String getProtocolId();

    String getSonalId();

    Observable<SonalDevicesResponse> getSonalDevices();

    Observable<SessionResponse> getSessions(int id);

    boolean getOnboardingDisplayed();

    void setOnboardingDisplayed();

    boolean getPrecautionsDisplayed();

    void setPrecautionsDisplayed();

}
