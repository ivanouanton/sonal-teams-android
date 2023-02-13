package com.waveneuro.ui.session.history

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.waveneuro.data.model.response.session.SessionResponse
import com.waveneuro.databinding.ActivitySessionHistoryBinding
import com.waveneuro.ui.base.BaseActivity
import com.waveneuro.ui.session.history.adapter.SessionListAdapter
import com.waveneuro.ui.session.session.SessionCommand
import timber.log.Timber
import java.sql.Timestamp
import java.text.SimpleDateFormat
import javax.inject.Inject

class SessionHistoryActivity : BaseActivity() {

    @Inject
    lateinit var sessionCommand: SessionCommand
    @Inject
    lateinit var sessionHistoryViewModel: SessionHistoryViewModel

    private lateinit var binding: ActivitySessionHistoryBinding
    private lateinit var adapter: SessionListAdapter

    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent()?.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivitySessionHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(SessionHistoryCommand.USER_ID)) {
            userId = intent.getIntExtra(SessionHistoryCommand.USER_ID, 0)
            binding.tvName.text = intent.getStringExtra(SessionHistoryCommand.NAME)
        }
        if (intent.hasExtra(SessionHistoryCommand.TREATMENT_DATA_PRESENT)) {
            binding.btnStartSession.isEnabled =
                intent.getBooleanExtra(SessionHistoryCommand.TREATMENT_DATA_PRESENT, false)
        }

        setView()
        setObserver()

        sessionHistoryViewModel.getSessionHistory(userId)
    }

    private fun setView() {
        with(binding) {
            btnStartSession.setOnClickListener {
                setResult(RESULT_OK, Intent().putExtra(START_SESSION, true))
                finish()
            }
            ivBack.setOnClickListener { onBackPressed() }
        }
    }

    private fun setObserver() {
        sessionHistoryViewModel.data.observe(this, viewStateObserver)
    }

    private val viewStateObserver = Observer { viewState: SessionResponse ->
        val sessions: MutableList<Session> = mutableListOf()
        for (i in viewState.sessions.indices) {
            val pattern = "dd/MM/yyyy"
            val simpleDateFormat = SimpleDateFormat(pattern)
            val session = viewState.sessions[i]
            val dateRd = session.eegRecordedAt?.let {
                val timestamp = Timestamp(Math.round(it) * 1000)
                simpleDateFormat.format(timestamp.time)
            } ?: ""
            val dateSd = session.finishedAt?.let {
                val timestamp = Timestamp(Math.round(it) * 1000)
                simpleDateFormat.format(timestamp.time)
            } ?: ""

            sessions.add(Session(session.sonalId, dateRd, dateSd, session.isCompleted))
        }

        adapter = SessionListAdapter(this, sessions)
        binding.rvSessions.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    companion object {
        const val START_SESSION = "start_session"
    }

}