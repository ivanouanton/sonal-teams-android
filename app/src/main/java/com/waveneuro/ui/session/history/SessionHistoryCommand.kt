package com.waveneuro.ui.session.history

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.asif.abase.base.NavigationCommand
import com.asif.abase.injection.qualifier.ActivityContext
import javax.inject.Inject

class SessionHistoryCommand @Inject constructor(
    @ActivityContext private val context: Context
) : NavigationCommand() {

    fun navigate(activity: Activity, userId: Int?, userUrl: String?,
                 firstName: String?, lastName: String?, treatmentDataPresent: Boolean) {
        val intent = Intent(context, SessionHistoryActivity::class.java)
        intent.putExtra(USER_ID, userId)
        intent.putExtra(USER_URL, userUrl)
        intent.putExtra(FIRST_NAME, firstName)
        intent.putExtra(LAST_NAME, lastName)
        intent.putExtra(TREATMENT_DATA_PRESENT, treatmentDataPresent)
        activity.startActivityForResult(intent, 0)
    }

    companion object {
        const val USER_ID = "user_id"
        const val USER_URL = "user_url"
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val TREATMENT_DATA_PRESENT = "treatment_data_present"
    }
}