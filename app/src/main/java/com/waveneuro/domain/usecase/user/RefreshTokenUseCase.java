package com.waveneuro.domain.usecase.user;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.response.user.RefreshResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class RefreshTokenUseCase extends ObservableUseCase<RefreshResponse> {

    private final DataManager dataManager;

    @Inject
    public RefreshTokenUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<RefreshResponse> buildUseCaseSingle() {
        return dataManager.refreshToken();
    }

}