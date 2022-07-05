package com.waveneuro.domain.usecase.password;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.password.password.SetPasswordRequest;
import com.waveneuro.data.model.response.password.password.SetPasswordResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class SetPasswordUseCase extends ObservableUseCase<SetPasswordResponse> {

    private final DataManager dataManager;

    private SetPasswordRequest request;

    @Inject
    public SetPasswordUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<SetPasswordResponse> buildUseCaseSingle() {
        return dataManager.setPassword(request);
    }

    public void execute(SetPasswordRequest request, UseCaseCallback<SetPasswordResponse> useCaseCallback) {
        this.request=request;
        super.execute(useCaseCallback);
    }

}