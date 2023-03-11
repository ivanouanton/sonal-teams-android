package com.waveneuro.domain.usecase.login;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.login.ConfirmTokenRequest;
import com.waveneuro.data.model.response.login.ConfirmTokenResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class ConfirmTokenUseCase extends ObservableUseCase<ConfirmTokenResponse> {

    private final DataManager dataManager;

    private ConfirmTokenRequest request;

    @Inject
    public ConfirmTokenUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<ConfirmTokenResponse> buildUseCaseSingle() {
        return dataManager.confirmToken(request);
    }

    public void execute(ConfirmTokenRequest request, UseCaseCallback<ConfirmTokenResponse> useCaseCallback) {
        this.request=request;
        super.execute(useCaseCallback);
    }

}