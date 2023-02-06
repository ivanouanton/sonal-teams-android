package com.waveneuro.domain.usecase.patient;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.response.patient.PatientResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class GetPatientUseCase extends ObservableUseCase<PatientResponse> {

    private final DataManager dataManager;

    private int id;

    @Inject
    public GetPatientUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<PatientResponse> buildUseCaseSingle() {
        return dataManager.patientWithId(id);
    }

    public void execute(int id, UseCaseCallback<PatientResponse> useCaseCallback) {
        this.id=id;
        super.execute(useCaseCallback);
    }

}