package com.waveneuro.ui.dashboard.history

import android.content.Context
import android.content.Intent
import com.asif.abase.base.NavigationCommand
import com.asif.abase.injection.qualifier.ActivityContext
import javax.inject.Inject

class HistoryCommand @Inject constructor(
    @ActivityContext private val context: Context
) : NavigationCommand() {

    override fun navigate() {
        val intent = Intent(context, HistoryActivity::class.java)
        context.startActivity(intent)
    }

}