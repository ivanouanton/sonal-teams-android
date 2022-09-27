package com.waveneuro.domain.usecase.patient;

import com.asif.abase.domain.base.ObservableUseCase;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.response.patient.PatientListResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class GetPatientsUseCase extends ObservableUseCase<PatientListResponse> {

    private final DataManager dataManager;

    @Inject
    public GetPatientsUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<PatientListResponse> buildUseCaseSingle() {
        return dataManager.patients();
    }

}