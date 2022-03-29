package com.waveneuro.ui.dashboard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.waveneuro.data.DataManager;
import com.waveneuro.data.analytics.AnalyticsEvent;
import com.waveneuro.data.analytics.AnalyticsManager;
import com.waveneuro.domain.base.SingleLiveEvent;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

public class DashBoardViewModel extends ViewModel {

    private final MutableLiveData<DashboardViewState> mDataLive = new MutableLiveData<>();
    private final SingleLiveEvent<DashboardViewEffect> mDataViewEffect = new SingleLiveEvent<>();

    @Inject
    AnalyticsManager analyticsManager;

    @Inject
    DataManager dataManager;

    @Inject
    public DashBoardViewModel() {
    }

    public void processEvent(DashboardViewEvent viewEvent) {
        if (viewEvent instanceof DashboardViewEvent.Connected) {
            DashboardViewEvent.Connected connected = (DashboardViewEvent.Connected) viewEvent;
            this.mDataLive.postValue(new DashboardViewState.Connect(connected.getBleDevice()));
        } else if (viewEvent instanceof DashboardViewEvent.Disconnected) {
            this.mDataLive.postValue(new DashboardViewState.Disconnect());
        } else if (viewEvent instanceof DashboardViewEvent.AccountClicked) {
            this.mDataViewEffect.postValue(new DashboardViewEffect.Account());
        } else if (viewEvent instanceof DashboardViewEvent.DeviceClicked) {
            if(this.getData().getValue() instanceof DashboardViewState.Connect) {
                DashboardViewState.Connect connect = (DashboardViewState.Connect) this.getData().getValue();
                this.mDataViewEffect.postValue(new DashboardViewEffect.Device(connect.getData().getName()));
            }
        }else if (viewEvent instanceof DashboardViewEvent.LogoutClicked) {
            sentLogoutEvent(dataManager.getUser().getUsername());
            logout();
            this.mDataViewEffect.postValue(new DashboardViewEffect.Login());
        }
    }

    private void sentLogoutEvent(String username) {
        JSONObject properties = new JSONObject();
        try {
            properties.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        analyticsManager.sendEvent(AnalyticsEvent.LOGIN, properties, AnalyticsManager.MIX_PANEL);
    }

    private void logout() {
        this.dataManager.logout();
    }

    public MutableLiveData<DashboardViewState> getData() {
        return mDataLive;
    }

    public SingleLiveEvent<DashboardViewEffect> getViewEffect() {
        return mDataViewEffect;
    }
}
