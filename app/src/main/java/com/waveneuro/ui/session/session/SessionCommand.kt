package com.waveneuro.ui.session.session

import android.content.Context
import android.content.Intent
import com.asif.abase.base.NavigationCommand
import com.asif.abase.data.model.BaseModel
import com.asif.abase.injection.qualifier.ActivityContext
import com.waveneuro.data.model.entity.BleDevice
import javax.inject.Inject

class SessionCommand @Inject constructor(
    @ActivityContext private val mContext: Context
) : NavigationCommand() {

    override fun navigate() {
        val intent = Intent(mContext, SessionActivity::class.java)
        mContext.startActivity(intent)
    }

    override fun navigate(model: BaseModel) {
        if (model is BleDevice) {
            val intent = Intent(mContext, SessionActivity::class.java)
            intent.putExtra(BLE_DEVICE, model)
            mContext.startActivity(intent)
        }
    }

    fun navigate(treatmentLength: String?, protocolFrequency: String?, sonalId: String?) {
        val intent = Intent(mContext, SessionActivity::class.java)
        intent.putExtra(SONAL_ID, sonalId)
        intent.putExtra(TREATMENT_LENGTH, treatmentLength)
        intent.putExtra(PROTOCOL_FREQUENCY, protocolFrequency)
        mContext.startActivity(intent)
    }

    companion object {
        const val BLE_DEVICE = "ble_device"
        const val TREATMENT_LENGTH = "treatment_length"
        const val PROTOCOL_FREQUENCY = "protocol_frequency"
        const val SONAL_ID = "sonal_id"
    }
}