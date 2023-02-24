package com.waveneuro.views

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.waveneuro.databinding.CustomProgressBinding
import com.waveneuro.databinding.SessionProgressBinding

fun Activity.waveProgressDialogBuilder() : AlertDialog {
    val binding = CustomProgressBinding.inflate(layoutInflater)
    val dialogBuilder = AlertDialog.Builder(this).setView(binding.root)
    dialogBuilder.setCancelable(false)
    val dialog = dialogBuilder.create()
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    return dialog
}

fun Activity.sessionProgressDialogBuilder() : AlertDialog {
    val binding = SessionProgressBinding.inflate(layoutInflater)
    val dialogBuilder = MaterialAlertDialogBuilder(this).setView(binding.root)
    dialogBuilder.setCancelable(false)
    val dialog = dialogBuilder.create()
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window?.setDimAmount(0.8f)

    return dialog
}