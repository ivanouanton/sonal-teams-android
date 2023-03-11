package com.waveneuro.ui.user.password.new_password

import android.content.Context
import android.content.Intent
import com.asif.abase.base.NavigationCommand
import com.asif.abase.injection.qualifier.ActivityContext
import javax.inject.Inject

class SetNewPasswordCommand @Inject constructor(
    @ActivityContext private val mContext: Context
) : NavigationCommand() {

    fun navigate(email: String?, code: String?) {
        val intent = Intent(mContext, SetNewPasswordActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(EMAIL, email)
        intent.putExtra(CODE, code)
        mContext.startActivity(intent)
    }

    companion object {
        const val EMAIL = "email"
        const val CODE = "code"
    }
}