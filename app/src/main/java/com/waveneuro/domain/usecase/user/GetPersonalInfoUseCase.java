package com.waveneuro.domain.usecase.user;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.response.user.UserInfoResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class GetPersonalInfoUseCase extends ObservableUseCase<UserInfoResponse> {

    private final DataManager dataManager;

    @Inject
    public GetPersonalInfoUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<UserInfoResponse> buildUseCaseSingle() {
        return dataManager.getPersonalInfo();
    }
    
}