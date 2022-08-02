package com.waveneuro.ui.dashboard.device;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.domain.base.SingleLiveEvent;
import com.waveneuro.domain.usecase.user.GetPersonalInfoUseCase;
import com.waveneuro.utils.ErrorUtil;

import javax.inject.Inject;

import timber.log.Timber;

public class DeviceViewModel extends ViewModel {

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;

    private final MutableLiveData<DeviceViewState> mDataLive = new MutableLiveData<>();
    private final SingleLiveEvent<DeviceViewEffect> mDataViewEffect = new SingleLiveEvent<>();
    private GetPersonalInfoUseCase getPersonalInfoUseCase;

    @Inject
    public DeviceViewModel(GetPersonalInfoUseCase getPersonalInfoUseCase) {
        this.getPersonalInfoUseCase = getPersonalInfoUseCase;
    }

    public void processEvent(DeviceViewEvent viewEvent) {
        Timber.e("DEVICE_EVENT :: %s", "" + (viewEvent.getClass().getSimpleName()));
        if (this.mDataLive.getValue() != null)
            Timber.e("DEVICE_STATE :: %s", "" + (this.mDataLive.getValue().getClass().getSimpleName()));
        if (viewEvent instanceof DeviceViewEvent.Start) {
//            this.mDataLive.postValue(new DeviceViewState.LocateDevice());
            this.mDataLive.postValue(new DeviceViewState.InitLocateDevice(dataManager.getUser()));
//            getPersonalInfo();
        } else if (viewEvent instanceof DeviceViewEvent.BackClicked) {


        } else if (viewEvent instanceof DeviceViewEvent.DeviceClicked) {
            DeviceViewEvent.DeviceClicked deviceClicked = (DeviceViewEvent.DeviceClicked) viewEvent;
            this.mDataLive.postValue(new DeviceViewState.Connecting(deviceClicked.getBleDevice()));
        } else if (viewEvent instanceof DeviceViewEvent.LocateDevice) {
            Timber.e("DEVICE_STATE :: %s", "" + (this.mDataLive.getValue() instanceof DeviceViewState.LocateDevice));
            this.mDataLive.postValue(new DeviceViewState.Searching());
        } else if (viewEvent instanceof DeviceViewEvent.Connected) {
            this.mDataLive.postValue(new DeviceViewState.Connected());
        } else if (viewEvent instanceof DeviceViewEvent.Disconnected) {
            this.mDataLive.postValue(new DeviceViewState.LocateDevice());
        } else if (viewEvent instanceof DeviceViewEvent.Searched) {
            this.mDataLive.postValue(new DeviceViewState.Searched());
        } else if (viewEvent instanceof DeviceViewEvent.NoDeviceFound) {
            this.mDataLive.postValue(new DeviceViewState.LocateDevice());
        } else if (viewEvent instanceof DeviceViewEvent.LocateDeviceNextClicked) {
            //TODO Searching device screen
            if (this.mDataLive.getValue() == null
                    || this.mDataLive.getValue() instanceof DeviceViewState.InitLocateDevice
                    || this.mDataLive.getValue() instanceof DeviceViewState.LocateDevice)
                this.mDataLive.postValue(new DeviceViewState.LocateDeviceNext());
            else
                this.mDataLive.postValue(new DeviceViewState.Searching());
        } else if (viewEvent instanceof DeviceViewEvent.StartSessionClicked) {
                this.mDataViewEffect.postValue(
                        new DeviceViewEffect.SessionRedirect(
                                dataManager.getTreatmentLength(),
                                dataManager.getProtocolFrequency(),
                                dataManager.getSonalId()));
            }
    }

    private void getPersonalInfo() {
        this.getPersonalInfoUseCase.execute(new UseCaseCallback() {
            @Override
            public void onSuccess(Object response) {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

    public void setDeviceId(String sonalId) {
        dataManager.saveSonalId(sonalId);
    }

    public MutableLiveData<DeviceViewState> getData() {
        return mDataLive;
    }

    public SingleLiveEvent<DeviceViewEffect> getViewEffect() {
        return mDataViewEffect;
    }

}