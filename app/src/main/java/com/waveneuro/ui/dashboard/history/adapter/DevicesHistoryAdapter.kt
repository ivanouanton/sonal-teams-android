package com.waveneuro.ui.dashboard.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.waveneuro.data.model.response.device.SonalDevicesResponse
import com.waveneuro.databinding.DevicesHistoryItemBinding

class DevicesHistoryAdapter(
    private val context: Context,
    private val devices: List<SonalDevicesResponse.Device>
) : RecyclerView.Adapter<DevicesHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: DevicesHistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val device = devices[bindingAdapterPosition]

            with(binding) {
                tvDeviceName.text = device.deviceName
                tvDeviceLastConnection.text = device.lastConnection
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(DevicesHistoryItemBinding.inflate(LayoutInflater.from(context), parent, false))

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind()
    }

    override fun getItemCount(): Int {
        return devices.size
    }

}
