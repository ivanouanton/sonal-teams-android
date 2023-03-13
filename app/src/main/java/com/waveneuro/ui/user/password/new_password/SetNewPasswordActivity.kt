package com.waveneuro.ui.user.password.new_password

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
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.waveneuro.R
import com.waveneuro.databinding.ActivitySetNewPasswordBinding
import com.waveneuro.ui.base.activity.BaseViewModelActivity
import com.waveneuro.ui.dashboard.DashboardCommand
import com.waveneuro.ui.user.login.LoginActivity
import com.waveneuro.ui.user.password.new_password.viewmodel.SetNewPasswordViewModel
import com.waveneuro.ui.user.password.new_password.viewmodel.SetNewPasswordViewModelImpl
import com.waveneuro.utils.ext.getAppComponent
import java.util.regex.Pattern
import javax.inject.Inject

class SetNewPasswordActivity :
    BaseViewModelActivity<ActivitySetNewPasswordBinding, SetNewPasswordViewModel>() {

    @Inject
    var dashboardCommand: DashboardCommand? = null

    override val viewModel: SetNewPasswordViewModelImpl by viewModels {
        getAppComponent().setNewPasswordViewModelFactory()
    }

    private var email: String? = null
    private var code: String? = null

    override fun initBinding(): ActivitySetNewPasswordBinding =
        ActivitySetNewPasswordBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
        super.onCreate(savedInstanceState)

        if (intent.hasExtra(EMAIL)
            && intent.hasExtra(CODE)
        ) {
            email = intent.getStringExtra(EMAIL)
            code = intent.getStringExtra(CODE)
        }
        setView()
        setObserver()
        binding.tipPassword.addTextChangedListener(mTextEditorWatcher)
    }

    private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val isLong = s.length >= 8
            val lowerAndUpper =
                Pattern.compile("[A-Z]").matcher(s).find() && Pattern.compile("[a-z]").matcher(s)
                    .find()
            val hasNumbers = Pattern.compile("[0-9]").matcher(s).find()
            val hasSpecials = Pattern.compile("[^a-zA-Z0-9]").matcher(s).find()
            val isCorrect = isLong && lowerAndUpper && hasNumbers && hasSpecials
            with(binding) {
                tvCondition1.setTextColor(resources.getColor(if (isLong) R.color.aqua else R.color.red))
                tvCondition2.setTextColor(resources.getColor(if (lowerAndUpper) R.color.aqua else R.color.red))
                tvCondition3.setTextColor(resources.getColor(if (hasNumbers) R.color.aqua else R.color.red))
                tvCondition4.setTextColor(resources.getColor(if (hasSpecials) R.color.aqua else R.color.red))
                btnResetPassword.isClickable = isCorrect
                btnResetPassword.setBackgroundColor(resources.getColor(if (isCorrect) R.color.aqua else R.color.gray))
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }

    private fun setView() {
        with(binding) {
            btnResetPassword.setOnClickListener {
                submit()
            }
            tvLogIn.setOnClickListener {
                startActivity(LoginActivity.newIntent(this@SetNewPasswordActivity))
            }
        }
        logInSpanText()
    }

    private fun setObserver() {
        viewModel.viewEffect.observe(this, Observer { viewEffect ->
            when (viewEffect) {
                is SetNewPasswordViewEffect.Success -> {
                    showSnackBar()
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

    private fun showSnackBar() {
        val snackBar =
            Snackbar.make(binding.root, R.string.password_has_been_changed, Snackbar.LENGTH_LONG)
        snackBar.duration = 100000000
        snackBar.setAction(R.string.ok) {
            snackBar.dismiss()
            startActivity(LoginActivity.newIntent(this@SetNewPasswordActivity))
        }
        snackBar.show()
    }

    private fun submit() {
        viewModel.processEvent(
            SetNewPasswordViewEvent.SetNewPassword(
                email ?: "",
                code ?: "",
                binding.tipPassword.text.toString()
            )
        )
    }

    companion object {
        private const val EMAIL = "email"
        private const val CODE = "code"

        fun newIntent(context: Context, email: String?, code: String?) = Intent(
            context,
            SetNewPasswordActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra(EMAIL, email)
            putExtra(CODE, code)
        }
    }
}