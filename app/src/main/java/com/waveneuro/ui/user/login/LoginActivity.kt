package com.waveneuro.ui.user.login

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.view.Window
import androidx.lifecycle.Observer
import com.waveneuro.R
import com.waveneuro.databinding.ActivityLoginBinding
import com.waveneuro.ui.base.BaseActivity
import com.waveneuro.ui.user.login.LoginViewEffect.*
import com.waveneuro.ui.user.login.LoginViewEvent.ClearRememberUser
import com.waveneuro.ui.user.login.LoginViewEvent.RememberUser
import com.waveneuro.ui.user.mfa.MfaCommand
import com.waveneuro.ui.user.password.password.confirm.SetNewPasswordCommand
import com.waveneuro.ui.user.password.reset.ResetPasswordCommand
import com.waveneuro.ui.user.registration.RegistrationCommand
import timber.log.Timber
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    @Inject
    lateinit var resetPasswordCommand: ResetPasswordCommand
    @Inject
    lateinit var registrationCommand: RegistrationCommand
    @Inject
    lateinit var setNewPasswordCommand: SetNewPasswordCommand
    @Inject
    lateinit var mfaCommand: MfaCommand
    @Inject
    lateinit var loginViewModel: LoginViewModel

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
        super.onCreate(savedInstanceState)
        activityComponent()?.inject(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setView()
        setObserver()
        loginViewModel.processEvent(LoginViewEvent.Start())
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
            loginViewModel.processEvent(LoginViewEvent.ForgotPasswordClicked())
        }
    }

    private fun setObserver() {
        loginViewModel.data.observe(this, loginViewStateObserver)
        loginViewModel.viewEffect.observe(this, loginViewEffectObserver)
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
        registrationCommand.navigate()
    }

    private fun login() {
        if (binding.chkRememberMe.isChecked) {
            loginViewModel.processEvent(
                RememberUser(binding.tipUsername.text.toString().trim())
            )
        } else {
            loginViewModel.processEvent(ClearRememberUser())
        }
        loginViewModel.processEvent(
            LoginViewEvent.LoginClicked(
                binding.tipUsername.text.toString().trim(),
                binding.tipPassword.text.toString().trim()
            )
        )
    }

}