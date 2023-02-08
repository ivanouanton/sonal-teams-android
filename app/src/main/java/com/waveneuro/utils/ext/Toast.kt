package com.waveneuro.utils.ext

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.toast(@StringRes messageStringRes: Int) {
    val toast = Toast.makeText(this, messageStringRes, Toast.LENGTH_SHORT)
    toast.show()
}

fun Context.toast(message: String) {
    val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast.show()
}