package com.waveneuro.ui.session.how_to;

import androidx.lifecycle.ViewModel;

import com.waveneuro.data.DataManager;
import com.waveneuro.domain.usecase.user.GetSonalDevicesUseCase;
import com.waveneuro.domain.usecase.user.PostSonalDevicesUseCase;
import com.waveneuro.utils.ErrorUtil;

import javax.inject.Inject;

public class HowToViewModel extends ViewModel {


    @Inject
    public HowToViewModel(GetSonalDevicesUseCase getSonalDevicesUseCase, PostSonalDevicesUseCase postSonalDevicesUseCase) {
    }

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;

}