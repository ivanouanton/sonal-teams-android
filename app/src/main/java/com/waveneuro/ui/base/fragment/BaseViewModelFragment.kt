package com.waveneuro.ui.base.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.waveneuro.ui.base.utils.EventObserver
import com.waveneuro.ui.base.viewmodel.BaseViewModel
import com.waveneuro.utils.ext.dialogBuilder
import com.waveneuro.utils.ext.toast

abstract class BaseViewModelFragment<B : ViewBinding, VM : BaseViewModel> :
    BaseFragmentNew<B>() {

    protected abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews(savedInstanceState)
        observeLiveData()
    }

    @CallSuper
    protected open fun initViews(savedInstanceState: Bundle?) {}

    @CallSuper
    protected open fun observeLiveData() {
        with(viewModel) {
            isLoading.observe(viewLifecycleOwner, Observer(::showLoading))
            message.observe(viewLifecycleOwner, EventObserver(::showMessage))
            logoutDone.observe(viewLifecycleOwner, EventObserver {
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
        requireActivity().dialogBuilder(title, message).show()
    }

    protected open fun showErrorDialog(@StringRes title: Int, @StringRes message: Int) {
        requireActivity().dialogBuilder(title, message).show()
    }

    protected open fun showMessage(message: String?) {
        requireActivity().toast(message)
    }

    private fun navigateToLoginScreen() {
        val intent = Intent( "action.user.login").setPackage(requireContext().packageName).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }

}
