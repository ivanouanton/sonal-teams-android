package com.waveneuro.data;

import android.text.TextUtils;

import com.waveneuro.data.model.entity.User;
import com.waveneuro.data.model.request.account.update.AccountUpdateRequest;
import com.waveneuro.data.model.request.email.forgot.ForgotUsernameRequest;
import com.waveneuro.data.model.request.login.LoginRequest;
import com.waveneuro.data.model.request.password.ResetPasswordRequest;
import com.waveneuro.data.model.request.password.confirm.ForgotPasswordConfirmRequest;
import com.waveneuro.data.model.request.password.password.ForgotPasswordRequest;
import com.waveneuro.data.model.request.password.password.SetNewPasswordRequest;
import com.waveneuro.data.model.request.password.password.SetPasswordRequest;
import com.waveneuro.data.model.request.treatment.AddTreatmentRequest;
import com.waveneuro.data.model.response.email.forgot.ForgotUsernameResponse;
import com.waveneuro.data.model.response.login.LoginResponse;
import com.waveneuro.data.model.response.password.ResetPasswordResponse;
import com.waveneuro.data.model.response.password.confirm.ForgotPasswordConfirmResponse;
import com.waveneuro.data.model.response.password.password.ForgotPasswordResponse;
import com.waveneuro.data.model.response.password.password.SetNewPasswordResponse;
import com.waveneuro.data.model.response.password.password.SetPasswordResponse;
import com.waveneuro.data.model.response.protocol.ProtocolResponse;
import com.waveneuro.data.model.response.treatment.TreatmentResponse;
import com.waveneuro.data.model.response.user.RefreshResponse;
import com.waveneuro.data.model.response.user.UserInfoResponse;
import com.waveneuro.data.preference.PreferenceManager;
import com.waveneuro.data.remote.TreatmentService;
import com.waveneuro.data.remote.UserService;

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
    public Observable<LoginResponse> login(LoginRequest request) {
        return this.userService.login(request);
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
    public Observable<ProtocolResponse> protocol() {
        return this.treatmentService.protocol();
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
    public String getTreatmentLength() {
        return preferenceManager.getTreatmentLength();
    }

    @Override
    public String getProtocolFrequency() {
        return preferenceManager.getProtocolFrequency();
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
