package com.waveneuro.domain.usecase.registration;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.login.LoginRequest;
import com.waveneuro.data.api.user.model.login.ApiLoginResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class RegistrationUseCase extends ObservableUseCase<ApiLoginResponse> {

    @Inject
    public RegistrationUseCase(DataManager dataManager) {
    }

    @Override
    public Observable<ApiLoginResponse> buildUseCaseSingle() {
        return null;
    }

    public void execute(LoginRequest request, UseCaseCallback<ApiLoginResponse> useCaseCallback) {
        super.execute(useCaseCallback);
    }

}
