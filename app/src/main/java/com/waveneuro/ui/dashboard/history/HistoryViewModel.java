package com.waveneuro.ui.dashboard.history;

import androidx.lifecycle.ViewModel;

import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.domain.usecase.user.GetSonalDevicesUseCase;
import com.waveneuro.domain.usecase.user.PostSonalDevicesUseCase;
import com.waveneuro.utils.ErrorUtil;

import javax.inject.Inject;

public class HistoryViewModel extends ViewModel {

    private GetSonalDevicesUseCase getSonalDevicesUseCase;
    private PostSonalDevicesUseCase postSonalDevicesUseCase;

    @Inject
    public HistoryViewModel(GetSonalDevicesUseCase getSonalDevicesUseCase, PostSonalDevicesUseCase postSonalDevicesUseCase) {
        this.getSonalDevicesUseCase = getSonalDevicesUseCase;
        this.postSonalDevicesUseCase = postSonalDevicesUseCase;
    }

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;

    private void getSonalDevices() {
        this.getSonalDevicesUseCase.execute(new UseCaseCallback() {
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


}