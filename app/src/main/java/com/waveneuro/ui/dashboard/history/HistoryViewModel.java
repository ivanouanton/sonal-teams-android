package com.waveneuro.ui.dashboard.history;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.asif.abase.data.model.APIError;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.response.device.SonalDevicesResponse;
import com.waveneuro.domain.usecase.user.GetSonalDevicesUseCase;
import com.waveneuro.utils.ErrorUtil;

import javax.inject.Inject;

public class HistoryViewModel extends ViewModel {

    private GetSonalDevicesUseCase getSonalDevicesUseCase;

    private final MutableLiveData<HistoryViewState> mDataLive = new MutableLiveData<>();

    @Inject
    public HistoryViewModel(GetSonalDevicesUseCase getSonalDevicesUseCase) {
        this.getSonalDevicesUseCase = getSonalDevicesUseCase;
    }

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;


    public void processEvent(HistoryViewEvent viewEvent) {
        if (viewEvent instanceof HistoryViewEvent.Start) {
            getSonalDevices();
        }
    }

    private void getSonalDevices() {
        this.getSonalDevicesUseCase.execute(new UseCaseCallback() {
            @Override
            public void onSuccess(Object response) {
                if (response instanceof SonalDevicesResponse) {
                    SonalDevicesResponse sonalDevicesResponse = (SonalDevicesResponse) response;
                    mDataLive.setValue(new HistoryViewState.Success(sonalDevicesResponse));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                APIError error = errorUtil.parseError(throwable);
                mDataLive.postValue(new HistoryViewState.Failure(error));
            }

            @Override
            public void onFinish() {
                mDataLive.postValue(new HistoryViewState.Loading(false));
            }
        });
    }


    public MutableLiveData<HistoryViewState> getData() {
        return mDataLive;
    }

}