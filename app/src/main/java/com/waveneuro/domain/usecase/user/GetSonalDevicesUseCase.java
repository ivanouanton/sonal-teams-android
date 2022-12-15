package com.waveneuro.domain.usecase.user;

import com.asif.abase.domain.base.ObservableUseCase;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.response.device.SonalDevicesResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class GetSonalDevicesUseCase extends ObservableUseCase<SonalDevicesResponse> {

    private final DataManager dataManager;

    @Inject
    public GetSonalDevicesUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<SonalDevicesResponse> buildUseCaseSingle() {
        return dataManager.getSonalDevices();
    }
    
}