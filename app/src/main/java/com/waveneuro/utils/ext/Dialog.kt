package com.waveneuro.utils.ext

import android.app.Activity
import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.waveneuro.R
import com.waveneuro.databinding.DialogPopupErrorBinding

fun Context.dialogBuilder(title: String, message: String): MaterialAlertDialogBuilder =
     MaterialAlertDialogBuilder(this, R.style.MaterialDialogGeneral)
         .setTitle(title)
         .setMessage(message)
         .setPositiveButton(R.string.ok) { dialog, _ -> dialog?.dismiss() }
         .setCancelable(true)

fun Context.dialogBuilder(@StringRes title: Int, @StringRes message: Int): MaterialAlertDialogBuilder =
    MaterialAlertDialogBuilder(this, R.style.MaterialDialogGeneral)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(R.string.ok) { dialog, _ -> dialog?.dismiss() }
        .setCancelable(true)

fun Activity.dialogPopUpErrorBuilder(title: String?, message: String, onClick: () -> Unit): AlertDialog {
    val binding = DialogPopupErrorBinding.inflate(layoutInflater)
    val builder = MaterialAlertDialogBuilder(this, R.style.PopUp).setView(binding.root)
    val dialog = builder.create()
    with(binding) {
        tvTitle.isVisible = title != null
        tvTitle.text = title
        tvContent.text = message
        btnPrimary.setOnClickListener {
            onClick()
            dialog.dismiss()
        }
    }
    return dialog
}
