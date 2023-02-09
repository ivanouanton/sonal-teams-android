package com.waveneuro.domain.usecase.patient;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.patient.PatientRequest;
import com.waveneuro.data.model.response.patient.PatientResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class UpdatePatientUseCase extends ObservableUseCase<PatientResponse> {

    private final DataManager dataManager;

    private PatientRequest request;
    private int id;

    @Inject
    public UpdatePatientUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<PatientResponse> buildUseCaseSingle() {
        return dataManager.updatePatientWithId(id, request);
    }

    public void execute(PatientRequest request, int id,  UseCaseCallback<PatientResponse> useCaseCallback) {
        this.request=request;
        this.id=id;
        super.execute(useCaseCallback);
    }

}