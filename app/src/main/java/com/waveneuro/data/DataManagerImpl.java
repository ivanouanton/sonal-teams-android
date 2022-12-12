package com.waveneuro.data;

import android.text.TextUtils;

import com.waveneuro.data.model.entity.User;
import com.waveneuro.data.model.request.account.update.AccountUpdateRequest;
import com.waveneuro.data.model.request.device.SonalDeviceRequest;
import com.waveneuro.data.model.request.email.forgot.ForgotUsernameRequest;
import com.waveneuro.data.model.request.login.ConfirmTokenRequest;
import com.waveneuro.data.model.request.login.LoginRequest;
import com.waveneuro.data.model.request.password.ResetPasswordRequest;
import com.waveneuro.data.model.request.password.confirm.ForgotPasswordConfirmRequest;
import com.waveneuro.data.model.request.password.password.ForgotPasswordRequest;
import com.waveneuro.data.model.request.password.password.SetNewPasswordRequest;
import com.waveneuro.data.model.request.password.password.SetPasswordRequest;
import com.waveneuro.data.model.request.patient.PatientRequest;
import com.waveneuro.data.model.request.treatment.AddTreatmentRequest;
import com.waveneuro.data.model.response.device.SonalDeviceResponse;
import com.waveneuro.data.model.response.email.forgot.ForgotUsernameResponse;
import com.waveneuro.data.model.response.login.ConfirmTokenResponse;
import com.waveneuro.data.model.response.login.LoginResponseMfa;
import com.waveneuro.data.model.response.password.ResetPasswordResponse;
import com.waveneuro.data.model.response.password.confirm.ForgotPasswordConfirmResponse;
import com.waveneuro.data.model.response.password.password.ForgotPasswordResponse;
import com.waveneuro.data.model.response.password.password.SetNewPasswordResponse;
import com.waveneuro.data.model.response.password.password.SetPasswordResponse;
import com.waveneuro.data.model.response.patient.PatientListResponse;
import com.waveneuro.data.model.response.patient.PatientResponse;
import com.waveneuro.data.model.response.protocol.ProtocolResponse;
import com.waveneuro.data.model.response.session.SessionResponse;
import com.waveneuro.data.model.response.treatment.TreatmentResponse;
import com.waveneuro.data.model.response.user.RefreshResponse;
import com.waveneuro.data.model.response.user.UserInfoResponse;
import com.waveneuro.data.preference.PreferenceManager;
import com.waveneuro.data.remote.TreatmentService;
import com.waveneuro.data.remote.UserService;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import timber.log.Timber;

public class DataManagerImpl implements DataManager {
    private final UserService userService;
    private final TreatmentService treatmentService;
    private final PreferenceManager preferenceManager;

    public DataManagerImpl(UserService userService, TreatmentService treatmentService, PreferenceManager preferenceManager) {
        this.userService = userService;
        this.treatmentService = treatmentService;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Observable<LoginResponseMfa> login(LoginRequest request) {
        return this.userService.login(request);
    }

    @Override
    public Observable<ConfirmTokenResponse> confirmToken(ConfirmTokenRequest request) {
        return this.userService.confirmSoftwareToken(request);
    }

    @Override
    public void saveAccessToken(String accessToken) {
        preferenceManager.setAccessToken(accessToken);
    }

    @Override
    public void saveRefreshToken(String refreshToken) {
        preferenceManager.setRefreshToken(refreshToken);
    }

    @Override
    public boolean isLoggedIn() {
        return !TextUtils.isEmpty(preferenceManager.getAccessToken());
    }

    @Override
    public void logout() {
        this.preferenceManager.logout();
    }

    @Override
    public Observable<ForgotUsernameResponse> forgotUsername(ForgotUsernameRequest request) {
        return null;
    }

    @Override
    public Observable<ResetPasswordResponse> resetPassword(ResetPasswordRequest request) {
        return null;
    }

    @Override
    public Observable<UserInfoResponse> getPersonalInfo() {
        return userService.getPersonalInfo();
    }

    @Override
    public Observable<ProtocolResponse> protocol(int id) {
        return this.userService.getProtocolForUser(id);
    }

    @Override
    public Observable<PatientListResponse> patients(Integer page, String startsWith, Integer[] ids) {
        return this.userService.getClientList(page, ids, startsWith);
    }

    @Override
    public Observable<List<PatientListResponse.Patient.Organization>> organizations() {
        return this.userService.getOrganizations();
    }

    @Override
    public Observable<PatientResponse> patientWithId(int id) {
        return this.userService.getClient(id);
    }

    @Override
    public Observable<PatientResponse> updatePatientWithId(int id, PatientRequest request) {
        return this.userService.updateClient(id, request);
    }

    @Override
    public Observable<ForgotPasswordResponse> forgotPassword(ForgotPasswordRequest request) {
        return this.userService.forgotPassword(request);
    }

    @Override
    public Observable<ForgotPasswordConfirmResponse> forgotPasswordConfirm(ForgotPasswordConfirmRequest request) {
        return this.userService.forgotPasswordConfirm(request);
    }

    @Override
    public Observable<TreatmentResponse> addTreatment(AddTreatmentRequest request) {
        Timber.e("ADD_TREATMENT :: %s", request.toString());
        return this.treatmentService.addTreatment(request);
//        return null;
    }

    @Override
    public Observable<RefreshResponse> refreshToken() {
        return this.userService.refreshToken();
    }

    @Override
    public void saveUser(UserInfoResponse user) {
        preferenceManager.saveUser(user);
    }

    @Override
    public void saveTreatmentLength(String treatmentLength) {
        preferenceManager.saveTreatmentLength(treatmentLength);
    }

    @Override
    public void saveProtocolFrequency(String protocolFrequency) {
        preferenceManager.saveProtocolFrequency(protocolFrequency);
    }

    @Override
    public void saveProtocolId(String protocolId) {
        preferenceManager.saveProtocolId(protocolId);
    }

    @Override
    public void saveSonalId(String sonalId) {
        preferenceManager.saveSonalId(sonalId);
    }

    @Override
    public String getTreatmentLength() {
        return preferenceManager.getTreatmentLength();
    }

    @Override
    public String getProtocolFrequency() {
        return preferenceManager.getProtocolFrequency();
    }

    @Override
    public String getProtocolId() {
        return preferenceManager.getProtocolId();
    }

    @Override
    public String getSonalId() {
        return preferenceManager.getSonalId();
    }

    @Override
    public Observable<List<SonalDeviceResponse>> getSonalDevices() {
        return userService.getSonalDevices();
    }

    @Override
    public Observable<SessionResponse> getSessions(int id) {
        return userService.getSessions(id);
    }

    @Override
    public boolean getOnboardingDisplayed() {
        return preferenceManager.getOnboardingDisplayed();
    }

    @Override
    public void setOnboardingDisplayed() {
        preferenceManager.setOnboardingDisplayed();
    }

    @Override
    public Observable<SonalDeviceResponse> postSonalDevice(SonalDeviceRequest newDevice) {
        return userService.addSonalDevice(newDevice);
    }

    @Override
    public String getEegId() {
        return preferenceManager.getEegId();
    }

    @Override
    public void saveEegId(String eegId) {
        preferenceManager.saveEegId(eegId);
    }

    @Override
    public Long getPatientId() {
        return preferenceManager.getPatientId();
    }

    @Override
    public void savePatientId(Long patientId) {
        preferenceManager.savePatientId(patientId);
    }

    @Override
    public User getUser() {
        User user = new User();
        user.setName(preferenceManager.getName());
        user.setImageThumbnailUrl(preferenceManager.getImageUrl());
        user.setUsername(preferenceManager.getUsername());
        return user;
    }

    @Override
    public Observable<SetNewPasswordResponse> setNewPassword(SetNewPasswordRequest request) {
        return this.userService.changeTempPassword(request);
    }

    @Override
    public Observable<SetPasswordResponse> setPassword(SetPasswordRequest request) {
        return this.userService.changePassword(request);
    }

    @Override
    public void rememberUsername(String username) {
        this.preferenceManager.rememberUsername(username);
    }

    @Override
    public void rememberPassword(String password) {
        this.preferenceManager.rememberPassword(password);
    }

    @Override
    public String getRememberUsername() {
        return preferenceManager.getRememberUsername();
    }

    @Override
    public String getRememberPassword() {
        return preferenceManager.getRememberPassword();
    }

    @Override
    public void removeRememberUser() {
        preferenceManager.removeRememberUser();
    }

    @Override
    public void removeRememberPassword() {
        preferenceManager.removeRememberPassword();
    }

    @Override
    public Observable<UserInfoResponse> updateUser(AccountUpdateRequest request) {
        return this.userService.updateUser(request);
    }


}
