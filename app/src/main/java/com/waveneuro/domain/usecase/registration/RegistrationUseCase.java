package com.waveneuro.domain.usecase.registration;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.login.LoginRequest;
import com.waveneuro.data.model.response.login.LoginResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class RegistrationUseCase extends ObservableUseCase {

    private final DataManager dataManager;

    @Inject
    public RegistrationUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<LoginResponse> buildUseCaseSingle() {
        return null;
    }

    public void execute(LoginRequest request, UseCaseCallback useCaseCallback) {
        super.execute(useCaseCallback);
    }

}
