package com.waveneuro.ui.user.password.reset

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.waveneuro.R
import com.waveneuro.databinding.ActivityResetPasswordBinding
import com.waveneuro.databinding.DialogPopupBinding
import com.waveneuro.ui.base.activity.BaseViewModelActivity
import com.waveneuro.ui.user.login.LoginActivity
import com.waveneuro.ui.user.login.LoginViewEffect
import com.waveneuro.ui.user.password.code.ForgotPasswordCodeActivity
import com.waveneuro.ui.user.password.reset.ResetPasswordViewEffect.LoginRedirect
import com.waveneuro.ui.user.password.reset.viewmodel.ResetPasswordViewModel
import com.waveneuro.ui.user.password.reset.viewmodel.ResetPasswordViewModelImpl
import com.waveneuro.utils.ext.getAppComponent
import com.waveneuro.utils.ext.toast

class ResetPasswordActivity : BaseViewModelActivity<ActivityResetPasswordBinding, ResetPasswordViewModel>() {

    override val viewModel: ResetPasswordViewModelImpl by viewModels {
        getAppComponent().resetPasswordViewModelFactory()
    }

    override fun initBinding(): ActivityResetPasswordBinding =
        ActivityResetPasswordBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
        super.onCreate(savedInstanceState)

        setView()
        setObserver()
    }

    private fun setView() {
        with(binding) {
            tvLogIn.setOnClickListener {
                startActivity(LoginActivity.newIntent(this@ResetPasswordActivity))
            }
            btnSendResetLink.setOnClickListener {
                if (tipUsername.text.isNullOrBlank()) {
                    toast("Enter email")
                } else {
                    viewModel.processEvent(ResetPasswordViewEvent.ForgotPasswordClicked(
                        tipUsername.text.toString().trim()
                    ))
                }
            }
        }
        logInSpanText()
        setupInputWatcher()
    }

    private fun logInSpanText() {
        val spannableString = SpannableString(getString(R.string.log_in))
        spannableString.setSpan(UnderlineSpan(), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.yellow_dim)),
            0,
            6,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        binding.tvLogIn.text = spannableString
    }

    private fun setupInputWatcher() {
        binding.etUsername.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.btnSendResetLink.isEnabled = count != 0
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun setObserver() {
        viewModel.viewEffect.observe(this, Observer { viewEffect ->
            when (viewEffect) {
                is ResetPasswordViewEffect.BackRedirect -> {
                    goBack()
                }
                is LoginRedirect -> {
                    launchLoginScreen()
                }
                is ResetPasswordViewEffect.Success -> {
                    launchCheckEmailDialog()
                }
                is ResetPasswordViewEffect.ShowErrorDialog -> {
                    showErrorDialog(viewEffect.title ?: "", viewEffect.message)
                }
            }
        })
    }

    private fun launchLoginScreen() {
        startActivity(LoginActivity.newIntent(this@ResetPasswordActivity))
        finish()
    }

    private fun launchCheckEmailDialog() {
        val dialogBinding = DialogPopupBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(this, R.style.PopUp).setView(dialogBinding.root)
        val dialog = builder.create()

        with(dialogBinding) {
            ibtnClose.isVisible = true
            tvTitle.setText(R.string.check_your_email)
            tvContent.setText(R.string.recovery_info)
            btnPrimary.setText(R.string.open_email_app)
            btnPrimary.setOnClickListener {
                dialog.dismiss()
                startActivity(ForgotPasswordCodeActivity.newIntent(
                    this@ResetPasswordActivity,
                    binding.tipUsername.text.toString().trim()
                ))

                try {
                    val intent = Intent(Intent.ACTION_MAIN)
                    intent.addCategory(Intent.CATEGORY_APP_EMAIL)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    this@ResetPasswordActivity.startActivity(intent)
                } catch (e: ActivityNotFoundException) { }
            }
            ibtnClose.setOnClickListener {
                dialog.dismiss()
                startActivity(ForgotPasswordCodeActivity.newIntent(
                    this@ResetPasswordActivity,
                    binding.tipUsername.text.toString().trim()
                ))
            }
        }

        dialog.show()
    }

    private fun goBack() {
        onBackPressed()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, ResetPasswordActivity::class.java)
    }

}