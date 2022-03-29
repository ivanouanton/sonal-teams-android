package com.waveneuro.ui.introduction.splash;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.response.user.UserInfoResponse;
import com.waveneuro.domain.base.SingleLiveEvent;
import com.waveneuro.domain.usecase.user.GetPersonalInfoUseCase;
import com.waveneuro.domain.usecase.user.RefreshTokenUseCase;

import javax.inject.Inject;

import timber.log.Timber;

public class SplashViewModel extends ViewModel {

    @Inject
    DataManager dataManager;

    private final MutableLiveData<SplashViewState> mDataLive = new MutableLiveData<>();
    private final SingleLiveEvent<SplashViewEffect> mDataViewEffect = new SingleLiveEvent<>();

    private final GetPersonalInfoUseCase getPersonalInfoUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    @Inject
    public SplashViewModel(GetPersonalInfoUseCase getPersonalInfoUseCase,
                           RefreshTokenUseCase refreshTokenUseCase) {
        this.getPersonalInfoUseCase = getPersonalInfoUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
    }

    public void processEvent(SplashViewEvent viewEvent) {
        if(viewEvent instanceof SplashViewEvent.Start) {
            if(dataManager.isLoggedIn()) {
                getPersonalInfo();
//                this.mDataViewEffect.setValue(new SplashViewEffect.Home());
            } else {
                this.mDataViewEffect.setValue(new SplashViewEffect.Login());
            }
        }

    }

    private void getPersonalInfo() {
        this.getPersonalInfoUseCase.execute(new UseCaseCallback() {
            @Override
            public void onSuccess(Object response) {
                Timber.e("PROFILE_SUCCESS");
                if(response instanceof UserInfoResponse) {
                    dataManager.saveUser((UserInfoResponse)response);
                    mDataViewEffect.setValue(new SplashViewEffect.Home());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e("PROFILE_FAILURE");
                mDataViewEffect.setValue(new SplashViewEffect.Login());
            }

            @Override
            public void onFinish() {

            }
        });
    }


    public MutableLiveData<SplashViewState> getData() {
        return mDataLive;
    }

    public SingleLiveEvent<SplashViewEffect> getViewEffect() {
        return mDataViewEffect;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
