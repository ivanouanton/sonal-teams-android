package com.waveneuro.ui.session.history;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.waveneuro.R;
import com.waveneuro.data.model.response.session.SessionResponse;
import com.waveneuro.ui.base.BaseActivity;
import com.waveneuro.ui.session.session.SessionCommand;
import com.waveneuro.ui.session.session.SessionViewState;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SessionHistoryActivity extends BaseActivity {
    
    public static final String START_SESSION = "start_session";

    @Inject
    SessionHistoryViewModel sessionHistoryViewModel;

    @BindView(R.id.rvSessions)
    RecyclerView rvSessions;

    protected SessionListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    int userId;

    @Inject
    SessionCommand sessionCommand;

    @OnClick(R.id.btn_start_session)
    public void onClickStartSession() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(START_SESSION, true);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        activityComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_history);
        ButterKnife.bind(this);

        if(getIntent().hasExtra(SessionHistoryCommand.USER_ID)) {
            userId = Integer.valueOf(getIntent().getStringExtra(SessionHistoryCommand.USER_ID));
            ((TextView)findViewById(R.id.tv_name)).setText(getIntent().getStringExtra(SessionHistoryCommand.NAME));
        }

        if(getIntent().hasExtra(SessionHistoryCommand.TREATMENT_DATA_PRESENT)) {
            ((MaterialButton)findViewById(R.id.btn_start_session)).setEnabled(getIntent().getBooleanExtra(SessionHistoryCommand.TREATMENT_DATA_PRESENT, false));
        }

        setView();
        setObserver();

        sessionHistoryViewModel.getSessionHistory(userId);
    }

    private void setView() {
        mLayoutManager = new LinearLayoutManager(this);
        rvSessions.setLayoutManager(mLayoutManager);
    }

    private void setObserver() {
        this.sessionHistoryViewModel.getData().observe(this, viewStateObserver);
    }

    @OnClick(R.id.iv_back)
    public void OnClickBack() {
        onBackPressed();
    }

    Observer<SessionResponse> viewStateObserver = viewState -> {

        List<Session> sessions = new ArrayList<>();
        for (int i = 0; i < viewState.getSessions().size(); i++) {
            String pattern = "dd/MM/yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            SessionResponse.Session session = viewState.getSessions().get(i);

            String dateRd;
            if (session.eegRecordedAt == null) {
                dateRd = "";
            } else {
                Timestamp timestamp = new Timestamp(Math.round(session.eegRecordedAt)*1000);
                dateRd = simpleDateFormat.format(timestamp.getTime());
            }

            String dateSd;
            if (session.finishedAt == null) {
                dateSd = "";
            } else {
                Timestamp timestamp = new Timestamp(Math.round(session.finishedAt)*1000);
                dateSd = simpleDateFormat.format(timestamp.getTime());
            }

            sessions.add(new Session(
                    session.getSonalId(),
                    dateRd,
                    dateSd,
                    session.isCompleted()));
        }
        mAdapter = new SessionListAdapter(sessions);
        rvSessions.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    };

}
