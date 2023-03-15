package com.waveneuro.domain.usecase.client;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.client.ClientRequest;
import com.waveneuro.data.model.response.client.ClientResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class UpdatePatientUseCase extends ObservableUseCase<ClientResponse> {

    private final DataManager dataManager;

    private ClientRequest request;
    private int id;

    @Inject
    public UpdatePatientUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<ClientResponse> buildUseCaseSingle() {
        return dataManager.updatePatientWithId(id, request);
    }

    public void execute(ClientRequest request, int id, UseCaseCallback<ClientResponse> useCaseCallback) {
        this.request=request;
        this.id=id;
        super.execute(useCaseCallback);
    }

}