package com.waveneuro.domain.usecase.protocol;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.response.protocol.ProtocolResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class GetLatestProtocolUseCase extends ObservableUseCase {

    private final DataManager dataManager;

    @Inject
    public GetLatestProtocolUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<ProtocolResponse> buildUseCaseSingle() {
        return dataManager.protocol();
    }

    public void execute(UseCaseCallback useCaseCallback) {
        super.execute(useCaseCallback);
    }
}