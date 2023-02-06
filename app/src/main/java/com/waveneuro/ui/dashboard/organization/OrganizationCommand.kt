package com.waveneuro.ui.dashboard.organization

import android.content.Context
import android.content.Intent
import com.asif.abase.base.NavigationCommand
import com.asif.abase.injection.qualifier.ActivityContext
import javax.inject.Inject

class OrganizationCommand @Inject constructor(
    @ActivityContext private val context: Context
) : NavigationCommand() {

    override fun navigate() {
        val intent = Intent(context, OrganizationActivity::class.java)
        context.startActivity(intent)
    }

}