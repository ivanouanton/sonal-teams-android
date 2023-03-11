package com.waveneuro.ui.user.login

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.view.Window
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.waveneuro.R
import com.waveneuro.databinding.ActivityLoginBinding
import com.waveneuro.ui.base.activity.BaseViewModelActivity
import com.waveneuro.ui.user.login.LoginViewEffect.*
import com.waveneuro.ui.user.login.LoginViewEvent.ClearRememberUser
import com.waveneuro.ui.user.login.LoginViewEvent.RememberUser
import com.waveneuro.ui.user.login.viewmodel.LoginViewModel
import com.waveneuro.ui.user.login.viewmodel.LoginViewModelImpl
import com.waveneuro.ui.user.mfa.MfaCommand
import com.waveneuro.ui.user.password.new_password.SetNewPasswordCommand
import com.waveneuro.ui.user.password.reset.ResetPasswordCommand
import com.waveneuro.ui.user.registration.RegistrationActivity
import com.waveneuro.utils.ext.getAppComponent
import timber.log.Timber
import javax.inject.Inject

class LoginActivity : BaseViewModelActivity<ActivityLoginBinding, LoginViewModel>() {

    @Inject
    lateinit var resetPasswordCommand: ResetPasswordCommand
    @Inject
    lateinit var setNewPasswordCommand: SetNewPasswordCommand
    @Inject
    lateinit var mfaCommand: MfaCommand
    @Inject
    lateinit var loginViewModelImpl: LoginViewModelImpl

    override fun initBinding(): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)

    override val viewModel: LoginViewModelImpl by viewModels {
        getAppComponent().loginViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
        super.onCreate(savedInstanceState)

        setView()
        setObserver()
        loginViewModelImpl.processEvent(LoginViewEvent.Start())
    }

    private fun setView() {
        recoverHereSpan()
        binding.btnLogin.setOnClickListener {
            if (validate()) login()
        }
    }

    private fun validate(): Boolean {
        with(binding) {
            var isError = false
            if (tipUsername.text.toString().isBlank()) {
                etUsername.error = getString(R.string.username_error)
                isError = true
            } else { etUsername.isErrorEnabled = false }

            if (tipPassword.text.toString().length < 8) {
                etPassword.error = getString(R.string.password_error)
                isError = true
            } else { etPassword.isErrorEnabled = false }

            return !isError
        }
    }

    private fun recoverHereSpan() {
        val spannableString = SpannableString(getString(R.string.forgot_password))
        spannableString.setSpan(UnderlineSpan(), 0, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.yellow_dim)),
            0,
            12,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        binding.tvRecoverHere.text = spannableString
        binding.tvRecoverHere.setOnClickListener {
            loginViewModelImpl.processEvent(LoginViewEvent.ForgotPasswordClicked())
        }
    }

    private fun setObserver() {
        loginViewModelImpl.data.observe(this, loginViewStateObserver)
        loginViewModelImpl.viewEffect.observe(this, loginViewEffectObserver)
    }

    private val loginViewStateObserver = Observer { viewState: LoginViewState ->
        when (viewState) {
            is LoginViewState.Success -> onSuccess(viewState.item)
            is LoginViewState.Failure -> onFailure(viewState.error)
            is LoginViewState.Loading -> {
                Timber.e("LOADING :: %s", viewState.loading)
                if (viewState.loading) displayWait("Loading...", null) else removeWait()
            }
        }
    }

    private val loginViewEffectObserver = Observer { viewEffect: LoginViewEffect ->
        when (viewEffect) {
            is ForgotPassword -> launchForgotPasswordScreen()
            is RememberMe -> setRememberData(viewEffect.username)
            is Register -> launchRegistrationScreen()
            is Support -> {}
            is SetNewPassword -> launchSetNewPasswordScreen()
            is EnterMfaCode -> {
                mfaCommand.navigate(
                    binding.etUsername.editText?.text.toString().trim(),
                    viewEffect.session
                )
            }
            else -> {}
        }
    }

    private fun setRememberData(username: String) {
        binding.tipUsername.setText(username)
        binding.chkRememberMe.isChecked = true
    }

    private fun launchSetNewPasswordScreen() {
        setNewPasswordCommand.navigate(
            binding.tipPassword.text.toString(),
            binding.tipPassword.text.toString()
        )
    }

    private fun launchForgotPasswordScreen() {
        resetPasswordCommand.navigate()
    }

    private fun launchRegistrationScreen() {
        startActivity(RegistrationActivity.newIntent(this))
    }

    private fun login() {
        if (binding.chkRememberMe.isChecked) {
            loginViewModelImpl.processEvent(
                RememberUser(binding.tipUsername.text.toString().trim())
            )
        } else {
            loginViewModelImpl.processEvent(ClearRememberUser())
        }
        loginViewModelImpl.processEvent(
            LoginViewEvent.LoginClicked(
                binding.tipUsername.text.toString().trim(),
                binding.tipPassword.text.toString().trim()
            )
        )
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }

}