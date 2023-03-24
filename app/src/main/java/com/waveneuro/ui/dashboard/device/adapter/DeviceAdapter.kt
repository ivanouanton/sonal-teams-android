package com.waveneuro.ui.dashboard.device.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.waveneuro.databinding.ListItemDeviceBinding
import com.waveneuro.domain.model.ble.BleDevice
import com.waveneuro.ui.base.recycler.BaseEntityListAdapter
import com.waveneuro.ui.base.recycler.EntityVH

class DeviceAdapter (
    context: Context,
    private val onItemClick: (BleDevice) -> Unit
) : BaseEntityListAdapter<BleDevice, DeviceAdapter.DeviceVH>(
    context,
    DiffCallback
) {

    inner class DeviceVH(
        private val binding: ListItemDeviceBinding
    ) : EntityVH<BleDevice>(binding.root) {
        @SuppressLint("SetTextI18n")
        override fun bind(model: BleDevice) {
            super.bind(model)
            binding.tvDeviceName.text = model.name
            binding.root.setOnClickListener { onItemClick(model) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceVH =
        DeviceVH(ListItemDeviceBinding.inflate(inflater, parent, false))

}

object DiffCallback : DiffUtil.ItemCallback<BleDevice>() {

    override fun areItemsTheSame(
        oldItem: BleDevice,
        newItem: BleDevice
    ): Boolean = oldItem.mac == newItem.mac

    override fun areContentsTheSame(
        oldItem: BleDevice,
        newItem: BleDevice
    ): Boolean = oldItem == newItem

}