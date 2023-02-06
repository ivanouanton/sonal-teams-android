package com.waveneuro.ui.base

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.asif.abase.base.APIStatusView
import com.asif.abase.base.ProgressWaitView
import com.asif.abase.data.model.APIError
import com.asif.abase.data.model.BaseError
import com.asif.abase.data.model.BaseModel
import com.waveneuro.R
import com.waveneuro.WaveNeuroApplication
import com.waveneuro.injection.component.ActivityComponent
import com.waveneuro.injection.component.DaggerPersistComponent
import com.waveneuro.injection.component.PersistComponent
import com.waveneuro.injection.module.ActivityModule
import com.waveneuro.views.WaveProgressDialog
import timber.log.Timber
import java.util.concurrent.atomic.AtomicLong

open class BaseActivity : AppCompatActivity(), ProgressWaitView, APIStatusView {

    private var mActivityComponent: ActivityComponent? = null
    private var waveProgressDialog: WaveProgressDialog? = null
    private var mActivityId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivityId = savedInstanceState?.getLong(KEY_ACTIVITY_ID)
            ?: NEXT_ID.getAndIncrement()
        mActivityComponent = getActivityComponent()
        waveProgressDialog = WaveProgressDialog(this)
    }

    private fun getActivityComponent(): ActivityComponent {
        val persistComponent: PersistComponent
        if (sComponentsMap.containsKey(mActivityId)) {
            Timber.i("Reusing ConfigPersistentComponent id=%d", mActivityId)
            persistComponent = sComponentsMap[mActivityId]!!
        } else {
            Timber.i("Creating new ConfigPersistentComponent id=%d", mActivityId)
            persistComponent = DaggerPersistComponent.builder()
                .applicationComponent((application as WaveNeuroApplication).appComponent)
                .build()
            sComponentsMap[mActivityId] = persistComponent
        }
        return persistComponent.activityComponent(ActivityModule(this))
    }

    fun activityComponent(): ActivityComponent? {
        if (mActivityComponent == null)
            mActivityComponent = getActivityComponent()
        return mActivityComponent
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(KEY_ACTIVITY_ID, mActivityId)
    }

    override fun onDestroy() {
        if (!isChangingConfigurations) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", mActivityId)
            sComponentsMap.remove(mActivityId)
        }
        super.onDestroy()
    }

    override fun onSuccess(model: BaseModel) {
        removeWait()
    }

    override fun onSuccess(model: List<BaseModel>) {
        removeWait()
    }

    override fun onFailure(error: BaseError) {
        val apiError = error as APIError
        var message: String? = getString(R.string.default_error_message)
        if (!TextUtils.isEmpty(apiError.message)) {
            message = apiError.message
        } else if (!TextUtils.isEmpty(apiError.msg)) {
            message = apiError.msg
        } else if (!TextUtils.isEmpty(apiError.error)) {
            message = apiError.error
        }
        removeWait()
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun displayWait() {
        if (waveProgressDialog != null && !isFinishing) {
            waveProgressDialog?.startProgressDialog()
        }
    }

    @JvmOverloads
    fun displayWait(title: String?, message: String? = null) {
        if (waveProgressDialog != null && !isFinishing) {
            waveProgressDialog?.startProgressDialog()
        }
    }

    override fun removeWait() {
        if (waveProgressDialog != null && waveProgressDialog!!.isShowing) {
            waveProgressDialog?.dismiss()
        }
    }

    companion object {
        private const val KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID"
        private val NEXT_ID = AtomicLong(0)
        private val sComponentsMap = mutableMapOf<Long, PersistComponent>()
    }
}