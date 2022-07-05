package com.waveneuro.ui.user.profile.notactive;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.waveneuro.data.DataManager;
import com.waveneuro.domain.base.SingleLiveEvent;
import com.waveneuro.utils.ErrorUtil;

import javax.inject.Inject;

public class AccountNotActiveViewModel extends ViewModel {

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;

    private final MutableLiveData<AccountNotActiveViewState> mDataLive = new MutableLiveData<>();
    private final SingleLiveEvent<AccountNotActiveViewEffect> mDataViewEffect = new SingleLiveEvent<>();

    void processEvent(AccountNotActiveViewEvent viewEvent) {
        if (viewEvent instanceof AccountNotActiveViewEvent.Start) {
        } else if (viewEvent instanceof AccountNotActiveViewEvent.BackClicked) {
            this.mDataViewEffect.postValue(new AccountNotActiveViewEffect.Back());
        }
    }

    public MutableLiveData<AccountNotActiveViewState> getData() {
        return mDataLive;
    }

    public SingleLiveEvent<AccountNotActiveViewEffect> getViewEffect() {
        return mDataViewEffect;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
