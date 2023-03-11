package com.waveneuro.ui.user.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.asif.abase.base.NavigationCommand
import com.asif.abase.injection.qualifier.ActivityContext
import javax.inject.Inject

class LoginCommand @Inject constructor(@param:ActivityContext private val mContext: Context) :
    NavigationCommand() {
    override fun navigate() {
        val intent = Intent(mContext, LoginActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        mContext.startActivity(intent)
        (mContext as Activity).finish()
    }
}