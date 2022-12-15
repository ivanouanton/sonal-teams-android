package com.waveneuro.ui.dashboard.history;

import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asif.abase.data.model.BaseModel;
import com.waveneuro.R;
import com.waveneuro.data.model.response.device.SonalDevicesResponse;
import com.waveneuro.ui.base.BaseActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HistoryActivity extends BaseActivity {
    @Inject
    HistoryViewModel historyViewModel;

    @BindView(R.id.rv_devices_history)
    RecyclerView rvDevicesHistory;
    RecyclerView.Adapter rvDevicesHistoryAdapter;

    private ArrayList<SonalDevicesResponse.Device> devices = new ArrayList<SonalDevicesResponse.Device>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        this.setObserver();

        this.historyViewModel.processEvent(new HistoryViewEvent.Start());

        rvDevicesHistoryAdapter = new DevicesHistoryAdapter(this, this.devices);

        this.rvDevicesHistory.setLayoutManager(new LinearLayoutManager(this));
        this.rvDevicesHistory.setAdapter(rvDevicesHistoryAdapter);
    }

    private void setView() {

    }

    private void setObserver() {
        this.historyViewModel.getData().observe(this, historyViewStateObserver);
    }


    Observer<HistoryViewState> historyViewStateObserver = viewState -> {
        if (viewState instanceof HistoryViewState.Success) {
            HistoryViewState.Success success = (HistoryViewState.Success) viewState;
            onSuccess(success.getData());
        } else if (viewState instanceof HistoryViewState.Failure) {
            HistoryViewState.Failure error = (HistoryViewState.Failure) viewState;
            onFailure(error.getError());
        } else if (viewState instanceof HistoryViewState.Loading) {
            HistoryViewState.Loading loading = ((HistoryViewState.Loading) viewState);
        }
    };

    @Override
    public void onSuccess(BaseModel model) {
        super.onSuccess(model);

        SonalDevicesResponse sonalDevicesResponse = (SonalDevicesResponse) model;

        this.devices.addAll(sonalDevicesResponse.getDevices());

        this.rvDevicesHistoryAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.iv_back)
    public void OnClickBack() {
        onBackPressed();
    }

}