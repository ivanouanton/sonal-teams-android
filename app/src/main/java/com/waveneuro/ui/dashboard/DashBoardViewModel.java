package com.waveneuro.ui.dashboard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.waveneuro.domain.base.SingleLiveEvent;

import javax.inject.Inject;

public class DashBoardViewModel extends ViewModel {

    private final MutableLiveData<DashboardViewState> mDataLive = new MutableLiveData<>();
    private final SingleLiveEvent<DashboardViewEffect> mDataViewEffect = new SingleLiveEvent<>();

    @Inject
    public DashBoardViewModel() {
    }

    public void processEvent(DashboardViewEvent viewEvent) {
        if (viewEvent instanceof DashboardViewEvent.Connected) {
            DashboardViewEvent.Connected connected = (DashboardViewEvent.Connected) viewEvent;
            this.mDataLive.postValue(new DashboardViewState.Connect(connected.getBleDevice()));
        } else if (viewEvent instanceof DashboardViewEvent.Disconnected) {
            this.mDataLive.postValue(DashboardViewState.Disconnect.INSTANCE);
        } else if (viewEvent instanceof DashboardViewEvent.AccountClicked) {
            this.mDataViewEffect.postValue(DashboardViewEffect.Account.INSTANCE);
        } else if (viewEvent instanceof DashboardViewEvent.DeviceClicked) {
            if(this.getData().getValue() instanceof DashboardViewState.Connect) {
                DashboardViewState.Connect connect = (DashboardViewState.Connect) this.getData().getValue();
                this.mDataViewEffect.postValue(new DashboardViewEffect.Device(connect.getData().getName()));
            }
        }
    }


    public MutableLiveData<DashboardViewState> getData() {
        return mDataLive;
    }

    public SingleLiveEvent<DashboardViewEffect> getViewEffect() {
        return mDataViewEffect;
    }
}
