package com.waveneuro.ui.session.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.waveneuro.databinding.ActivitySessionHistoryBinding
import com.waveneuro.ui.base.activity.BaseViewModelActivity
import com.waveneuro.ui.model.Session
import com.waveneuro.ui.session.history.adapter.SessionListAdapter
import com.waveneuro.ui.session.history.viewmodel.SessionHistoryViewModel
import com.waveneuro.ui.session.history.viewmodel.SessionHistoryViewModelImpl
import com.waveneuro.utils.ext.getAppComponent
import java.sql.Timestamp
import java.text.SimpleDateFormat

class SessionHistoryActivity :
    BaseViewModelActivity<ActivitySessionHistoryBinding, SessionHistoryViewModel>() {

    private lateinit var adapter: SessionListAdapter

    private var userId = 0

    override val viewModel: SessionHistoryViewModelImpl by viewModels {
        getAppComponent().sessionHistoryViewModelFactory()
    }

    override fun initBinding(): ActivitySessionHistoryBinding =
        ActivitySessionHistoryBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra(USER_ID)) {
            userId = intent.getIntExtra(USER_ID, 0)
        }
        if (intent.hasExtra(TREATMENT_DATA_PRESENT)) {
            binding.btnStartSession.isEnabled =
                intent.getBooleanExtra(TREATMENT_DATA_PRESENT, false)
        }

        setView()
        setObserver()

        viewModel.processEvent(SessionHistoryViewEvent.Start(userId))
    }

    private fun setView() {
        with(binding) {
            val firstName = intent.getStringExtra(FIRST_NAME) ?: ""
            val lastName = intent.getStringExtra(LAST_NAME) ?: ""
            val url = intent.getStringExtra(USER_URL) ?: ""

            tvName.text = "$firstName $lastName"
            btnStartSession.setOnClickListener {
                setResult(RESULT_OK, Intent().putExtra(START_SESSION, true))
                finish()
            }
            ivBack.setOnClickListener { onBackPressed() }

            if (url.isBlank()) {
                val initials = "${firstName[0]}${lastName[0]}".trim().uppercase()

                tvInitials.text = initials
                tvInitials.isVisible = true
            } else {
                Glide.with(this@SessionHistoryActivity)
                    .load(url)
                    .into(binding.ivProfileImage)
            }
        }
    }

    private fun setObserver() {
        viewModel.viewEffect.observe(this, Observer { viewEffect ->
            when(viewEffect) {
                is SessionHistoryViewEffect.Success -> {
                    val sessions: MutableList<Session> = mutableListOf()
                    viewEffect.sessionList.sessions.forEach { session ->
                        val pattern = "dd/MM/yyyy"
                        val simpleDateFormat = SimpleDateFormat(pattern)
                        val dateRd = session.eegRecordedAt.let {
                            val timestamp = Timestamp(Math.round(it) * 1000)
                            simpleDateFormat.format(timestamp.time)
                        } ?: ""
                        val dateSd = session.finishedAt.let {
                            val timestamp = Timestamp(Math.round(it) * 1000)
                            simpleDateFormat.format(timestamp.time)
                        } ?: ""

                        sessions.add(Session(session.sonalId, dateRd, dateSd, session.isCompleted))
                    }

                    adapter = SessionListAdapter(this, sessions)
                    binding.rvSessions.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    companion object {
        const val START_SESSION = "start_session"

        private const val USER_ID = "user_id"
        private const val USER_URL = "user_url"
        private const val FIRST_NAME = "first_name"
        private const val LAST_NAME = "last_name"
        private const val TREATMENT_DATA_PRESENT = "treatment_data_present"

        fun newIntent(context: Context, userId: Int?, userUrl: String?,
                     firstName: String?, lastName: String?, treatmentDataPresent: Boolean) =
            Intent(context, SessionHistoryActivity::class.java).apply {
                putExtra(USER_ID, userId)
                putExtra(USER_URL, userUrl)
                putExtra(FIRST_NAME, firstName)
                putExtra(LAST_NAME, lastName)
                putExtra(TREATMENT_DATA_PRESENT, treatmentDataPresent)
            }
    }

}