package com.waveneuro.ui.session.history;

import android.os.Bundle;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.waveneuro.R;
import com.waveneuro.data.model.response.session.SessionResponse;
import com.waveneuro.ui.base.BaseActivity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SessionHistoryActivity extends BaseActivity {

    @Inject
    SessionHistoryViewModel sessionHistoryViewModel;

    @BindView(R.id.rvSessions)
    RecyclerView rvSessions;

    protected SessionListAdapter mAdapter;

    protected RecyclerView.LayoutManager mLayoutManager;

    int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_history);
        ButterKnife.bind(this);

        if(getIntent().hasExtra(SessionHistoryCommand.USER_ID)) {
            userId = Integer.valueOf(getIntent().getStringExtra(SessionHistoryCommand.USER_ID));
            ((TextView)findViewById(R.id.tvName)).setText(getIntent().getStringExtra(SessionHistoryCommand.NAME));
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
            Timestamp rd = new Timestamp(Math.round(viewState.getSessions().get(i).eegRecordedAt)*1000);
            Timestamp sd = new Timestamp(Math.round(viewState.getSessions().get(i).finishedAt)*1000);
            String dateRd = simpleDateFormat.format(rd.getTime());
            String dateSd = simpleDateFormat.format(sd.getTime());
            sessions.add(new Session(
                    viewState.getSessions().get(i).getSonalId(),
                    "EEG R/D: " + dateRd,
                    "S/D: " + dateSd,
                    viewState.getSessions().get(i).isCompleted()?"Completed":"Terminated"));

        }
        mAdapter = new SessionListAdapter(sessions);
        rvSessions.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    };
}