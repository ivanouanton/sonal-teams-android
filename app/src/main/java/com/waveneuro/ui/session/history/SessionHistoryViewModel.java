package com.waveneuro.ui.session.history;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.response.session.SessionResponse;
import com.waveneuro.ui.session.session.SessionViewState;
import com.waveneuro.utils.ErrorUtil;

import javax.inject.Inject;

public class SessionHistoryViewModel extends ViewModel {

    private GetSessionHistoryUseCase getSessionHistoryUseCase;
    private final MutableLiveData<SessionResponse> mDataLive = new MutableLiveData<>();


    @Inject
    public SessionHistoryViewModel(GetSessionHistoryUseCase useCase) {
        this.getSessionHistoryUseCase = useCase;
    }

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;

    public void getSessionHistory(int id) {
        this.getSessionHistoryUseCase.execute(id, new UseCaseCallback() {
            @Override
            public void onSuccess(Object response) {
                mDataLive.postValue((SessionResponse) response);
            }

            @Override
            public void onError(Throwable throwable) {
                int x = 0;
            }

            @Override
            public void onFinish() {
                int x = 0;
            }
        });
    }

    public MutableLiveData<SessionResponse> getData() {
        return mDataLive;
    }



}