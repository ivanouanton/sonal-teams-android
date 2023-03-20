package com.waveneuro.ui.base.activity

import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.waveneuro.ui.base.utils.EventObserver
import com.waveneuro.ui.base.viewmodel.BaseViewModel
import com.waveneuro.utils.ext.dialogBuilder
import com.waveneuro.utils.ext.toast

abstract class BaseViewModelActivity<B : ViewBinding, VM : BaseViewModel> :
    BaseActivity<B>() {

    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeLiveData()
    }

    @CallSuper
    protected open fun observeLiveData() {
        with(viewModel) {
            isLoading.observe(this@BaseViewModelActivity, Observer(::showLoading))
            message.observe(this@BaseViewModelActivity, EventObserver(::showMessage))
            logoutDone.observe(this@BaseViewModelActivity, EventObserver {
                navigateToLoginScreen()
            })
        }
    }

    protected open fun showLoading(flag: Boolean?) {
        if (flag == true) {
            if (waveProgressDialog?.isShowing != true)
                waveProgressDialog?.show()
        } else {
            waveProgressDialog?.dismiss()
        }
    }

    protected open fun showErrorDialog(title: String, message: String) {
        dialogBuilder(title, message).show()
    }

    protected open fun showErrorDialog(@StringRes title: Int, @StringRes message: Int) {
        dialogBuilder(title, message).show()
    }

    protected open fun showMessage(message: String?) {
        toast(message)
    }

    private fun navigateToLoginScreen() {
        val intent = Intent( "action.user.login").setPackage(this.packageName).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

}