package com.waveneuro.domain.usecase.password;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.password.password.SetNewPasswordRequest;
import com.waveneuro.data.model.response.password.password.SetNewPasswordResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class SetNewPasswordUseCase extends ObservableUseCase {

    private final DataManager dataManager;

    private SetNewPasswordRequest request;

    @Inject
    public SetNewPasswordUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<SetNewPasswordResponse> buildUseCaseSingle() {
        return dataManager.setNewPassword(request);
    }

    public void execute(SetNewPasswordRequest request, UseCaseCallback useCaseCallback) {
        this.request=request;
        super.execute(useCaseCallback);
    }

}