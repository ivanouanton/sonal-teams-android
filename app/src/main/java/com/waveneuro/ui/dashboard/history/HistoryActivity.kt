package com.waveneuro.ui.dashboard.history

import android.os.Bundle
import androidx.lifecycle.Observer
import com.asif.abase.data.model.BaseModel
import com.waveneuro.data.model.response.device.SonalDevicesResponse
import com.waveneuro.databinding.ActivityHistoryBinding
import com.waveneuro.ui.base.BaseActivity
import com.waveneuro.ui.dashboard.history.adapter.DevicesHistoryAdapter
import javax.inject.Inject

class HistoryActivity : BaseActivity() {

    @Inject
    lateinit var historyViewModel: HistoryViewModel

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var adapter: DevicesHistoryAdapter

    private val devices = mutableListOf<SonalDevicesResponse.Device>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent()?.inject(this)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setView()
        setObserver()

        historyViewModel.processEvent(HistoryViewEvent.Start)
    }

    private fun setView() {
        adapter = DevicesHistoryAdapter(this, devices)
        binding.rvDevicesHistory.adapter = adapter
        binding.ivBack.setOnClickListener { onBackPressed() }
    }

    private fun setObserver() {
        historyViewModel.data.observe(this, historyViewStateObserver)
    }

    private val historyViewStateObserver = Observer { viewState: HistoryViewState? ->
        when (viewState) {
            is HistoryViewState.Success -> {
                onSuccess(viewState.data)
            }
            is HistoryViewState.Failure -> {
                onFailure(viewState.error)
            }
            is HistoryViewState.Loading -> {
                val loading = viewState
            }
            else -> {}
        }
    }

    override fun onSuccess(model: BaseModel) {
        super.onSuccess(model)
        if (model is SonalDevicesResponse) {
            devices.addAll(model.devices)
            adapter.notifyDataSetChanged()
        }
    }

}