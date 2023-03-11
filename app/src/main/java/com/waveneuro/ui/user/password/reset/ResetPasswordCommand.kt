package com.waveneuro.ui.user.password.reset

import android.content.Context
import android.content.Intent
import com.asif.abase.base.NavigationCommand
import com.asif.abase.injection.qualifier.ActivityContext
import javax.inject.Inject

class ResetPasswordCommand @Inject constructor(@param:ActivityContext private val mContext: Context) :
    NavigationCommand() {
    override fun navigate() {
        val intent = Intent(mContext, ResetPasswordActivity::class.java)
        mContext.startActivity(intent)
    }
}