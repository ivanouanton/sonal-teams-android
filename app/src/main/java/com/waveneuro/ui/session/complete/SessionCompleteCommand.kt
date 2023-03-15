package com.waveneuro.ui.session.complete

import android.content.Context
import android.content.Intent
import com.asif.abase.base.NavigationCommand
import com.asif.abase.injection.qualifier.ActivityContext
import javax.inject.Inject

class SessionCompleteCommand @Inject constructor(@param:ActivityContext private val mContext: Context) :
    NavigationCommand() {
    override fun navigate() {
        val intent = Intent(mContext, SessionCompleteActivity::class.java)
        mContext.startActivity(intent)
    }
}