package com.waveneuro.domain.usecase.password;

import com.asif.abase.data.model.BaseModel;
import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.password.confirm.ForgotPasswordConfirmRequest;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class ChangePasswordUseCase extends ObservableUseCase<BaseModel> {

    @Inject
    public ChangePasswordUseCase(DataManager dataManager) {
    }

    @Override
    public Observable<BaseModel> buildUseCaseSingle() {
        return null;
    }

    public void execute(ForgotPasswordConfirmRequest request, UseCaseCallback<BaseModel> useCaseCallback) {
        super.execute(useCaseCallback);
    }

}