package com.waveneuro.domain.usecase.treatment;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.treatment.AddTreatmentRequest;
import com.waveneuro.data.model.response.treatment.TreatmentResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class AddTreatmentUseCase extends ObservableUseCase {

    private final DataManager dataManager;

    private AddTreatmentRequest request;

    @Inject
    public AddTreatmentUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<TreatmentResponse> buildUseCaseSingle() {
        return dataManager.addTreatment(request);
    }

    public void execute(AddTreatmentRequest request, UseCaseCallback useCaseCallback) {
        this.request = request;
        super.execute(useCaseCallback);
    }
}