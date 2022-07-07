package com.waveneuro.domain.usecase.sentemail;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.email.forgot.ForgotUsernameRequest;
import com.waveneuro.data.model.response.email.forgot.ForgotUsernameResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class ForgotUsernameUseCase extends ObservableUseCase<ForgotUsernameResponse> {

    private final DataManager dataManager;

    private ForgotUsernameRequest request;

    @Inject
    public ForgotUsernameUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<ForgotUsernameResponse> buildUseCaseSingle() {
        return dataManager.forgotUsername(request);
    }

    public void execute(ForgotUsernameRequest request, UseCaseCallback<ForgotUsernameResponse> useCaseCallback) {
        this.request=request;
        super.execute(useCaseCallback);
    }

}
