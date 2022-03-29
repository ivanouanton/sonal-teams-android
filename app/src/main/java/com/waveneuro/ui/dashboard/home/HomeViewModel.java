package com.waveneuro.ui.dashboard.home;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.asif.abase.data.model.APIError;
import com.asif.abase.domain.base.UseCaseCallback;
import com.asif.abase.exception.SomethingWrongException;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.response.protocol.ProtocolResponse;
import com.waveneuro.domain.base.SingleLiveEvent;
import com.waveneuro.domain.usecase.protocol.GetLatestProtocolUseCase;
import com.waveneuro.utils.ErrorUtil;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;

    private final MutableLiveData<HomeDeviceViewState> mDataDeviceLive = new MutableLiveData<>();
    private final MutableLiveData<HomeUserViewState> mDataUserLive = new MutableLiveData<>();
    private final MutableLiveData<HomeProtocolViewState> mDataProtocolLive = new MutableLiveData<>();
    private final SingleLiveEvent<HomeViewEffect> mDataViewEffect = new SingleLiveEvent<>();

    private final GetLatestProtocolUseCase getLatestProtocolUseCase;

    @Inject
    public HomeViewModel(GetLatestProtocolUseCase getLatestProtocolUseCase) {
        this.getLatestProtocolUseCase = getLatestProtocolUseCase;
    }

    public void processEvent(HomeViewEvent viewEvent) {
        if (viewEvent instanceof HomeViewEvent.Start) {
            if (this.mDataDeviceLive.getValue() instanceof HomeDeviceViewState.StartSession) {
                return;
            }
            this.mDataDeviceLive.postValue(new HomeDeviceViewState.PairDevice());
            getUserDetails();
            getProtocol();
        } else if (viewEvent instanceof HomeViewEvent.DeviceDisconnected) {
            this.mDataDeviceLive.postValue(new HomeDeviceViewState.PairDevice());
        } else if (viewEvent instanceof HomeViewEvent.DeviceConnected) {
            this.mDataDeviceLive.postValue(new HomeDeviceViewState.StartSession());
        } else if (viewEvent instanceof HomeViewEvent.StartSessionClicked) {
            if (this.mDataDeviceLive.getValue() instanceof HomeDeviceViewState.PairDevice) {
                this.mDataViewEffect.postValue(new HomeViewEffect.DeviceRedirect());
            } else {
                this.mDataViewEffect.postValue(new HomeViewEffect.DeviceRedirect());
//                this.mDataViewEffect.postValue(
//                        new HomeViewEffect.SessionRedirect(
//                                dataManager.getTreatmentLength(),
//                                dataManager.getProtocolFrequency()));
            }
        }
    }

    private void getUserDetails() {
        this.mDataUserLive.postValue(new HomeUserViewState.Success(dataManager.getUser()));
    }

    private void getProtocol() {
        mDataProtocolLive.postValue(new HomeProtocolViewState.Loading(true));
        this.getLatestProtocolUseCase.execute(new UseCaseCallback() {
            @Override
            public void onSuccess(Object response) {
                mDataProtocolLive.postValue(new HomeProtocolViewState.Loading(false));
                ProtocolResponse protocolResponse = (ProtocolResponse) response;
                if (protocolResponse.getError() != null && TextUtils.isEmpty(protocolResponse.getError())) {
                    APIError error = errorUtil.parseError(new SomethingWrongException(), protocolResponse.getError());
                    mDataProtocolLive.postValue(new HomeProtocolViewState.Failure(error));
                } else {
                    dataManager.saveTreatmentLength(protocolResponse.getTreatmentLength());
                    dataManager.saveProtocolFrequency(protocolResponse.getProtocolFrequency());
                    dataManager.saveEegId(protocolResponse.getEegId());
                    mDataProtocolLive.postValue(new HomeProtocolViewState.Success(protocolResponse));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                APIError error = errorUtil.parseError(new SomethingWrongException());
                mDataProtocolLive.postValue(new HomeProtocolViewState.Loading(false));
                mDataProtocolLive.postValue(new HomeProtocolViewState.Failure(error));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    public MutableLiveData<HomeDeviceViewState> getDeviceData() {
        return mDataDeviceLive;
    }

    public MutableLiveData<HomeUserViewState> getUserData() {
        return mDataUserLive;
    }

    public LiveData<HomeProtocolViewState> getProtocolData() {
        return mDataProtocolLive;
    }

    public SingleLiveEvent<HomeViewEffect> getViewEffect() {
        return mDataViewEffect;
    }
}
