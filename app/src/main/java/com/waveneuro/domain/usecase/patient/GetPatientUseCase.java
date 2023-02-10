package com.waveneuro.domain.usecase.patient;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.response.client.ClientResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class GetPatientUseCase extends ObservableUseCase<ClientResponse> {

    private final DataManager dataManager;

    private int id;

    @Inject
    public GetPatientUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<ClientResponse> buildUseCaseSingle() {
        return dataManager.patientWithId(id);
    }

    public void execute(int id, UseCaseCallback<ClientResponse> useCaseCallback) {
        this.id=id;
        super.execute(useCaseCallback);
    }

}