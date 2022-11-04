package com.waveneuro.domain.usecase.patient;

import com.asif.abase.domain.base.ObservableUseCase;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.response.patient.PatientListResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class GetOrganizationsUseCase extends ObservableUseCase<List<PatientListResponse.Patient.Organization>> {

    private final DataManager dataManager;

    @Inject
    public GetOrganizationsUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<List<PatientListResponse.Patient.Organization>> buildUseCaseSingle() {
        return dataManager.organizations();
    }


}