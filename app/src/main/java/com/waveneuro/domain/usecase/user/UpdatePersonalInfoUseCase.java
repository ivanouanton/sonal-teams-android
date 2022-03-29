package com.waveneuro.domain.usecase.user;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.account.update.AccountUpdateRequest;
import com.waveneuro.data.model.response.user.UserInfoResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class UpdatePersonalInfoUseCase extends ObservableUseCase {

    private final DataManager dataManager;

    private AccountUpdateRequest request;
    @Inject
    public UpdatePersonalInfoUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<UserInfoResponse> buildUseCaseSingle() {
        return dataManager.updateUser(request);
    }

    public void execute(AccountUpdateRequest request,UseCaseCallback useCaseCallback) {
        this.request = request;
        super.execute(useCaseCallback);
    }
}