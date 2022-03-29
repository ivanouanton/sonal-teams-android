package com.waveneuro.domain.usecase.password;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.password.password.ForgotPasswordRequest;
import com.waveneuro.data.model.response.password.password.ForgotPasswordResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class ForgotPasswordUseCase extends ObservableUseCase {

    private final DataManager dataManager;

    private ForgotPasswordRequest request;

    @Inject
    public ForgotPasswordUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<ForgotPasswordResponse> buildUseCaseSingle() {
        return dataManager.forgotPassword(request);
    }

    public void execute(ForgotPasswordRequest request, UseCaseCallback useCaseCallback) {
        this.request=request;
        super.execute(useCaseCallback);
    }

}