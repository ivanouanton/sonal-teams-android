package com.waveneuro.ui.dashboard.account;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.asif.abase.data.model.APIError;
import com.asif.abase.domain.base.UseCaseCallback;
import com.asif.abase.exception.SomethingWrongException;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.request.account.update.AccountUpdateRequest;
import com.waveneuro.data.model.response.user.UserInfoResponse;
import com.waveneuro.domain.base.SingleLiveEvent;
import com.waveneuro.domain.usecase.user.GetPersonalInfoUseCase;
import com.waveneuro.domain.usecase.user.UpdatePersonalInfoUseCase;
import com.waveneuro.utils.ErrorUtil;

import javax.inject.Inject;

public class AccountViewModel extends ViewModel {

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;

    private final MutableLiveData<AccountViewState> mDataLive = new MutableLiveData<>();
    private final SingleLiveEvent<AccountViewEffect> mDataViewEffect = new SingleLiveEvent<>();
    private final GetPersonalInfoUseCase getPersonalInfoUseCase;
    private final UpdatePersonalInfoUseCase updatePersonalInfoUseCase;

    @Inject
    public AccountViewModel(GetPersonalInfoUseCase getPersonalInfoUseCase,
                            UpdatePersonalInfoUseCase updatePersonalInfoUseCase) {
        this.getPersonalInfoUseCase = getPersonalInfoUseCase;
        this.updatePersonalInfoUseCase = updatePersonalInfoUseCase;
    }

    public void processEvent(AccountViewEvent viewEvent) {
        if (viewEvent instanceof AccountViewEvent.Start) {
            getPersonalInfo();
        } else if (viewEvent instanceof AccountViewEvent.UpdatedUser) {
            AccountViewEvent.UpdatedUser updatedUser = (AccountViewEvent.UpdatedUser) viewEvent;
            updateUser(updatedUser.getFirstName(), updatedUser.getLastName(),
                    updatedUser.getUsername(), updatedUser.getEmail(), updatedUser.getDob());
        } else if (viewEvent instanceof AccountViewEvent.BackClicked) {
            this.mDataViewEffect.postValue(new AccountViewEffect.BackRedirect());
        }
    }

    private void updateUser(String givenName, String familyName, String username,
                            String email, String birthDate) {
        AccountUpdateRequest accountUpdateRequest = new AccountUpdateRequest();
        accountUpdateRequest.setGivenName(givenName);
        accountUpdateRequest.setFamilyName(familyName);
        accountUpdateRequest.setUsername(username);
        accountUpdateRequest.setEmail(email);
        accountUpdateRequest.setBirthDate(birthDate);
        mDataLive.postValue(new AccountViewState.Loading(true));
        this.updatePersonalInfoUseCase.execute(accountUpdateRequest, new UseCaseCallback() {
            @Override
            public void onSuccess(Object response) {
                mDataLive.postValue(new AccountViewState.Loading(false));
                if (response instanceof UserInfoResponse) {
                    UserInfoResponse userInfoResponse = (UserInfoResponse) response;
                    if (userInfoResponse.getError() != null
                            && TextUtils.isEmpty(userInfoResponse.getError())) {
                        mDataLive.postValue(new AccountViewState.Loading(false));
                        APIError error = errorUtil.parseError(new SomethingWrongException(),
                                userInfoResponse.getError());
                        mDataLive.postValue(new AccountViewState.Failure(error));
                    } else {
                        dataManager.saveUser(userInfoResponse);
                        mDataLive.setValue(new AccountViewState.Success(userInfoResponse));
                    }
                }
                mDataViewEffect.postValue(new AccountViewEffect.UpdateSuccess());
            }

            @Override
            public void onError(Throwable throwable) {
                mDataLive.postValue(new AccountViewState.Loading(false));
                APIError error = errorUtil.parseError(throwable);
                mDataLive.postValue(new AccountViewState.Failure(error));
            }

            @Override
            public void onFinish() {

            }
        });

    }

    private void getPersonalInfo() {
        mDataLive.postValue(new AccountViewState.Loading(true));
        this.getPersonalInfoUseCase.execute(new UseCaseCallback() {
            @Override
            public void onSuccess(Object response) {
                mDataLive.postValue(new AccountViewState.Loading(false));
                if (response instanceof UserInfoResponse) {
                    UserInfoResponse userInfoResponse = (UserInfoResponse) response;
                    if (userInfoResponse.getError() != null
                            && TextUtils.isEmpty(userInfoResponse.getError())) {
                        mDataLive.postValue(new AccountViewState.Loading(false));
                        APIError error = errorUtil.parseError(new SomethingWrongException(),
                                userInfoResponse.getError());
                        mDataLive.postValue(new AccountViewState.Failure(error));
                    } else {
                        dataManager.saveUser(userInfoResponse);
                        mDataLive.setValue(new AccountViewState.Success(userInfoResponse));
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                mDataLive.postValue(new AccountViewState.Loading(false));
                APIError error = errorUtil.parseError(throwable);
                mDataLive.postValue(new AccountViewState.Failure(error));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    public MutableLiveData<AccountViewState> getData() {
        return mDataLive;
    }

    public SingleLiveEvent<AccountViewEffect> getViewEffect() {
        return mDataViewEffect;
    }

}
