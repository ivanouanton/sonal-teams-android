package com.waveneuro.domain.usecase.password;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.response.password.ResetPasswordResponse;
import com.waveneuro.data.model.request.password.ResetPasswordRequest;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class ResetPasswordUseCase extends ObservableUseCase {

    private final DataManager dataManager;

    private ResetPasswordRequest request;

    @Inject
    public ResetPasswordUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<ResetPasswordResponse> buildUseCaseSingle() {
        return dataManager.resetPassword(request);
    }

    public void execute(ResetPasswordRequest request, UseCaseCallback useCaseCallback) {
        this.request=request;
        super.execute(useCaseCallback);
    }

}