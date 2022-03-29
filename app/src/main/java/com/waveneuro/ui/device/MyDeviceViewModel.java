package com.waveneuro.ui.device;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.waveneuro.domain.base.SingleLiveEvent;

import javax.inject.Inject;

public class MyDeviceViewModel extends ViewModel {

    private final MutableLiveData<MyDeviceViewState> mDataLive = new MutableLiveData<>();
    private final SingleLiveEvent<MyDeviceViewEffect> mDataViewEffect = new SingleLiveEvent<>();


    @Inject
    public MyDeviceViewModel() {
    }

    void processEvent(MyDeviceViewEvent viewEvent) {
        if (viewEvent instanceof MyDeviceViewEvent.Start) {

        } else if (viewEvent instanceof MyDeviceViewEvent.BackClicked) {
            this.mDataViewEffect.postValue(new MyDeviceViewEffect.BackRedirect());
        }
    }


    public MutableLiveData<MyDeviceViewState> getData() {
        return mDataLive;
    }

    public SingleLiveEvent<MyDeviceViewEffect> getViewEffect() {
        return mDataViewEffect;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

}
