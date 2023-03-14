package com.waveneuro.ui.dashboard.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.waveneuro.databinding.ActivityHistoryBinding
import com.waveneuro.domain.model.device.Device
import com.waveneuro.ui.base.activity.BaseViewModelActivity
import com.waveneuro.ui.dashboard.history.adapter.DevicesHistoryAdapter
import com.waveneuro.ui.dashboard.history.viewmodel.HistoryViewModel
import com.waveneuro.ui.dashboard.history.viewmodel.HistoryViewModelImpl
import com.waveneuro.utils.ext.getAppComponent

class HistoryActivity : BaseViewModelActivity<ActivityHistoryBinding, HistoryViewModel>() {

    private lateinit var adapter: DevicesHistoryAdapter

    private val devices = mutableListOf<Device>()

    override val viewModel: HistoryViewModelImpl by viewModels {
        getAppComponent().historyViewModelFactory()
    }

    override fun initBinding(): ActivityHistoryBinding =
        ActivityHistoryBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setView()
        setObserver()

        viewModel.processEvent(HistoryViewEvent.Start)
    }

    private fun setView() {
        adapter = DevicesHistoryAdapter(this, devices)
        binding.rvDevicesHistory.adapter = adapter
        binding.ivBack.setOnClickListener { onBackPressed() }
    }

    private fun setObserver() {
        viewModel.viewEffect.observe(this, Observer { viewState ->
            when (viewState) {
                is HistoryViewEffect.Success -> {
                    onSuccess(viewState.deviceList)
                }
            }
        })
    }


    private fun onSuccess(deviceList: List<Device>) {
        devices.addAll(deviceList)
        adapter.notifyDataSetChanged()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, HistoryActivity::class.java)
    }
}