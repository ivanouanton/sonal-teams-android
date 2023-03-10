package com.waveneuro.domain.usecase.login;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.login.LoginRequest;
import com.waveneuro.data.model.response.login.LoginResponse;
import com.waveneuro.data.model.response.login.LoginResponseMfa;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class LoginUseCase extends ObservableUseCase<LoginResponseMfa> {

    private final DataManager dataManager;

    private LoginRequest request;

    @Inject
    public LoginUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<LoginResponseMfa> buildUseCaseSingle() {
        return dataManager.login(request);
    }

    public void execute(LoginRequest request, UseCaseCallback<LoginResponseMfa> useCaseCallback) {
        this.request=request;
        super.execute(useCaseCallback);
    }

}