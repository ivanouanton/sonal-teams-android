package com.waveneuro.domain.usecase.user;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.device.SonalDeviceRequest;
import com.waveneuro.data.model.request.treatment.AddTreatmentRequest;
import com.waveneuro.data.model.response.device.SonalDeviceResponse;
import com.waveneuro.data.model.response.treatment.TreatmentResponse;
import com.waveneuro.data.model.response.user.UserInfoResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class PostSonalDevicesUseCase extends ObservableUseCase<SonalDeviceResponse> {

    private final DataManager dataManager;

    private SonalDeviceRequest request;

    @Inject
    public PostSonalDevicesUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<SonalDeviceResponse> buildUseCaseSingle() {
        return dataManager.postSonalDevice(request);
    }

    public void execute(SonalDeviceRequest request, UseCaseCallback<SonalDeviceRequest> useCaseCallback) {
        this.request = request;
        super.execute(useCaseCallback);
    }
}