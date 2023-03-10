package com.waveneuro.ui.base.activity

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.waveneuro.views.waveProgressDialogBuilder

abstract class BaseActivityNew<B : ViewBinding> : AppCompatActivity() {

    protected val binding: B by lazy { initBinding() }

    protected var waveProgressDialog: AlertDialog? = null

    protected abstract fun initBinding(): B


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        waveProgressDialog = this.waveProgressDialogBuilder()
    }

    override fun onPause() {
        waveProgressDialog?.dismiss()
        super.onPause()
    }

}
