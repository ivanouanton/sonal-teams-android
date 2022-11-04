package com.waveneuro.ui.session.how_to;

import androidx.lifecycle.ViewModel;

import com.waveneuro.data.DataManager;
import com.waveneuro.domain.usecase.user.GetSonalDevicesUseCase;
import com.waveneuro.domain.usecase.user.PostSonalDevicesUseCase;
import com.waveneuro.utils.ErrorUtil;

import javax.inject.Inject;

public class HowToViewModel extends ViewModel {

    private GetSonalDevicesUseCase getSonalDevicesUseCase;
    private PostSonalDevicesUseCase postSonalDevicesUseCase;

    @Inject
    public HowToViewModel(GetSonalDevicesUseCase getSonalDevicesUseCase, PostSonalDevicesUseCase postSonalDevicesUseCase) {
        this.getSonalDevicesUseCase = getSonalDevicesUseCase;
        this.postSonalDevicesUseCase = postSonalDevicesUseCase;
    }

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;



}