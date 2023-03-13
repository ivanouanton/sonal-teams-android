package com.waveneuro.ui.device

import android.content.Context
import android.content.Intent
import javax.inject.Inject
import com.asif.abase.injection.qualifier.ActivityContext
import com.asif.abase.base.NavigationCommand
import com.waveneuro.ui.device.MyDeviceActivity
import com.waveneuro.ui.device.MyDeviceCommand

class MyDeviceCommand @Inject constructor(@param:ActivityContext private val mContext: Context) :
    NavigationCommand() {
    override fun navigate(value: String) {
        val intent = Intent(mContext, MyDeviceActivity::class.java)
        intent.putExtra(DEVICE_NAME, value)
        mContext.startActivity(intent)
    }

    companion object {
        const val DEVICE_NAME = "device_name"
    }
}