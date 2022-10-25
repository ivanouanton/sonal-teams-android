package com.waveneuro.ui.session.history;

import com.asif.abase.domain.base.ObservableUseCase;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.response.session.SessionResponse;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class GetSessionHistoryUseCase extends ObservableUseCase<SessionResponse> {

    private final DataManager dataManager;

    private int id;

    @Inject
    public GetSessionHistoryUseCase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public Observable<SessionResponse> buildUseCaseSingle() {
        return dataManager.getSessions(id);
    }

    public void execute(int id, UseCaseCallback<SessionResponse> useCaseCallback) {
        this.id = id;
        super.execute(useCaseCallback);
    }
    
}