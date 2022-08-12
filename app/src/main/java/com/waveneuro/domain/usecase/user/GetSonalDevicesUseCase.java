package com.waveneuro.domain.usecase.user;

import com.asif.abase.domain.base.ObservableUseCase;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.response.device.SonalDeviceResponse;
import com.waveneuro.data.model.response.user.UserInfoResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class GetSonalDevicesUseCase extends ObservableUseCase<List<SonalDeviceResponse>> {

    private final DataManager dataManager;

    @Inject
    public GetSonalDevicesUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<List<SonalDeviceResponse>> buildUseCaseSingle() {
        return dataManager.getSonalDevices();
    }
    
}