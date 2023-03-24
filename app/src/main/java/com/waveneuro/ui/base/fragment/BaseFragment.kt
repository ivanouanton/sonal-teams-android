package com.waveneuro.ui.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.waveneuro.views.waveProgressDialogBuilder

abstract class BaseFragment<B : ViewBinding> : Fragment() {

    protected var binding: B? = null

    protected var waveProgressDialog: AlertDialog? = null

    protected abstract fun initBinding(container: ViewGroup?): B

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = initBinding(container)
        waveProgressDialog = requireActivity().waveProgressDialogBuilder()

        return binding?.root
    }

    override fun onPause() {
        waveProgressDialog?.dismiss()
        super.onPause()
    }

    override fun onDestroyView() {
        binding = null

        super.onDestroyView()
    }

}
