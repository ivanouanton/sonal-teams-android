package com.waveneuro.domain.usecase.patient;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.response.email.forgot.ForgotUsernameResponse;
import com.waveneuro.data.model.response.patient.PatientListResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class GetPatientsUseCase extends ObservableUseCase<PatientListResponse> {

    private final DataManager dataManager;

    private Integer page;
    private Integer[] ids;
    private String startsWith;

    @Inject
    public GetPatientsUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<PatientListResponse> buildUseCaseSingle() {
        return dataManager.patients(page, startsWith, ids);
    }

    public void execute(Integer page, String starsWith, Integer[] ids, UseCaseCallback<ForgotUsernameResponse> useCaseCallback) {
        this.page = page;
        this.ids=ids;
        this.startsWith = starsWith;
        super.execute(useCaseCallback);
    }
}