package com.waveneuro.ui.dashboard.history;

import android.os.Bundle;
import com.waveneuro.R;
import com.waveneuro.ui.base.BaseActivity;
import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class HistoryActivity extends BaseActivity {
    @Inject
    HistoryViewModel historyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        activityComponent().inject(this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_back)
    public void OnClickBack() {
        onBackPressed();
    }

}