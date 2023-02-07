package com.waveneuro.ui.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.asif.abase.base.NavigationCommand
import com.asif.abase.injection.qualifier.ActivityContext
import javax.inject.Inject

class DashboardCommand @Inject constructor(
    @ActivityContext private val context: Context
) : NavigationCommand() {

    override fun navigate() {
        val intent = Intent(context, DashboardActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
        if (context is Activity) context.finish()
    }

}