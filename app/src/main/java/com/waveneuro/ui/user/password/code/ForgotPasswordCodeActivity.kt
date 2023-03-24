package com.waveneuro.ui.user.password.code

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.waveneuro.R
import com.waveneuro.databinding.ActivityForgotPasswordCodeBinding
import com.waveneuro.ui.base.activity.BaseViewModelActivity
import com.waveneuro.ui.dashboard.DashboardActivity
import com.waveneuro.ui.dashboard.web.WebActivity
import com.waveneuro.ui.dashboard.web.WebActivity.Companion.PAGE_SUPPORT
import com.waveneuro.ui.user.login.LoginActivity
import com.waveneuro.ui.user.password.code.viewmodel.ForgotPasswordCodeViewModel
import com.waveneuro.ui.user.password.code.viewmodel.ForgotPasswordCodeViewModelImpl
import com.waveneuro.ui.user.password.new_password.SetNewPasswordActivity
import com.waveneuro.utils.ext.getAppComponent
import com.waveneuro.utils.ext.toast

internal class ForgotPasswordCodeActivity :
    BaseViewModelActivity<ActivityForgotPasswordCodeBinding, ForgotPasswordCodeViewModel>() {

    override val viewModel: ForgotPasswordCodeViewModelImpl by viewModels {
        getAppComponent().forgotPasswordCodeViewModelFactory()
    }

    private var username: String? = ""
    private val viewsList: MutableList<TextInputEditText> = mutableListOf()
    private var currentFocusedPosition = 1

    override fun initBinding(): ActivityForgotPasswordCodeBinding =
        ActivityForgotPasswordCodeBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
        super.onCreate(savedInstanceState)

        if (intent.hasExtra(EMAIL)) {
            username = intent.getStringExtra(EMAIL)
        }
        viewsList.add(findViewById(R.id.tip_code1))
        viewsList.add(findViewById(R.id.tip_code2))
        viewsList.add(findViewById(R.id.tip_code3))
        viewsList.add(findViewById(R.id.tip_code4))
        viewsList.add(findViewById(R.id.tip_code5))
        viewsList.add(findViewById(R.id.tip_code6))
        for (i in viewsList.indices) {
            viewsList[i].addTextChangedListener(mTextEditorWatcher)
            viewsList[i].setOnKeyListener(onKeyListener)
        }
        viewsList[0].requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        setView()
        setObservers()
    }

    private fun setView() {
        with(binding) {
            tvResendCode.setOnClickListener {
                viewModel.processEvent(ForgotPasswordCodeViewEvent.ResetPassword(username))
            }
            tvLogIn.setOnClickListener {
                startActivity(LoginActivity.newIntent(this@ForgotPasswordCodeActivity))
            }
            tvSupport.setOnClickListener {
                startActivity(WebActivity.newIntent(this@ForgotPasswordCodeActivity, PAGE_SUPPORT))
            }
        }

        logInSpanText()
        supportSpanText()
        resendSpanText()
    }

    private fun setObservers() {
        viewModel.viewEffect.observe(this, Observer { viewEffect ->
            when (viewEffect) {
                is ForgotPasswordCodeViewEffect.Home -> {
                    launchHomeScreen()
                }
                is ForgotPasswordCodeViewEffect.WrongMfaCode -> {
                    toast("Wrong code")
                }
            }
        })
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

    private fun supportSpanText() {
        val spannableString = SpannableString(getString(R.string.support))
        spannableString.setSpan(UnderlineSpan(), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.yellow_dim)),
            0,
            7,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        binding.tvSupport.text = spannableString
    }

    private fun resendSpanText() {
        val spannableString = SpannableString(getString(R.string.resend_code))
        spannableString.setSpan(UnderlineSpan(), 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.yellow_dim)),
            0,
            11,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        binding.tvResendCode.text = spannableString
    }

    private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.length == 1) {
                if (currentFocusedPosition < 6) {
                    viewsList[currentFocusedPosition].requestFocus()
                    currentFocusedPosition++
                } else {
                    startActivity(
                        SetNewPasswordActivity.newIntent(
                            this@ForgotPasswordCodeActivity,
                            username,
                            buildCode()
                        )
                    )
                }
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }

    private fun buildCode(): String {
        val mfaCode = StringBuffer()
        for (i in viewsList.indices) {
            mfaCode.append(viewsList[i].text.toString())
        }
        return mfaCode.toString()
    }

    private val onKeyListener =
        View.OnKeyListener { view, i, keyEvent -> //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if (i == KeyEvent.KEYCODE_DEL) {
                if (currentFocusedPosition > 1) {
                    viewsList[currentFocusedPosition - 2].requestFocus()
                    currentFocusedPosition--
                }
            }
            false
        }


    private fun launchHomeScreen() {
        startActivity(DashboardActivity.newIntent(this))
        finish()
    }

    companion object {
        private const val EMAIL = "email"

        fun newIntent(context: Context, email: String) = Intent(context, ForgotPasswordCodeActivity::class.java).apply {
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EMAIL, email)
        }
    }

}