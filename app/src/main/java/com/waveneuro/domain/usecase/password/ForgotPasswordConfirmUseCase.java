package com.waveneuro.domain.usecase.password;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.password.confirm.ForgotPasswordConfirmRequest;
import com.waveneuro.data.model.response.password.confirm.ForgotPasswordConfirmResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class ForgotPasswordConfirmUseCase extends ObservableUseCase<ForgotPasswordConfirmResponse> {

    private final DataManager dataManager;

    private ForgotPasswordConfirmRequest request;

    @Inject
    public ForgotPasswordConfirmUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<ForgotPasswordConfirmResponse> buildUseCaseSingle() {
        return dataManager.forgotPasswordConfirm(request);
    }

    public void execute(ForgotPasswordConfirmRequest request, UseCaseCallback<ForgotPasswordConfirmResponse> useCaseCallback) {
        this.request=request;
        super.execute(useCaseCallback);
    }

}