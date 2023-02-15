package com.waveneuro.ui.user.mfa

import android.content.Context
import android.content.Intent
import com.asif.abase.base.NavigationCommand
import com.asif.abase.injection.qualifier.ActivityContext
import javax.inject.Inject

class MfaCommand @Inject constructor(
    @ActivityContext private val context: Context
) : NavigationCommand() {

    fun navigate(username: String?, session: String?) {
        val intent = Intent(context, MfaActivity::class.java)
        intent.putExtra(USERNAME, username)
        intent.putExtra(SESSION, session)
        context.startActivity(intent)
    }

    companion object {
        const val USERNAME = "username"
        const val SESSION = "session"
    }
}