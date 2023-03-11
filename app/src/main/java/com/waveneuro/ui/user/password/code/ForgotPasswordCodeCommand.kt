package com.waveneuro.ui.user.password.code

import android.content.Context
import android.content.Intent
import com.asif.abase.base.NavigationCommand
import com.asif.abase.injection.qualifier.ActivityContext
import javax.inject.Inject

class ForgotPasswordCodeCommand @Inject constructor(@param:ActivityContext private val mContext: Context) :
    NavigationCommand() {
    override fun navigate(email: String) {
        val intent = Intent(mContext, ForgotPasswordCodeActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(EMAIL, email)
        mContext.startActivity(intent)
    }

    companion object {
        const val EMAIL = "email"
    }
}