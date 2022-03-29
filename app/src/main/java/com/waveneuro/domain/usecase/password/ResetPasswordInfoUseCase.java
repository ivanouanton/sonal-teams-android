package com.waveneuro.domain.usecase.password;

import com.asif.abase.data.model.BaseModel;
import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.password.confirm.ForgotPasswordConfirmRequest;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class ResetPasswordInfoUseCase extends ObservableUseCase {

    private final DataManager dataManager;

    private ForgotPasswordConfirmRequest request;

    @Inject
    public ResetPasswordInfoUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<BaseModel> buildUseCaseSingle() {
        return null;
    }

    public void execute(ForgotPasswordConfirmRequest request, UseCaseCallback useCaseCallback) {
        this.request=request;
        super.execute(useCaseCallback);
    }

}