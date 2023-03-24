package com.waveneuro.ui.user.mfa

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.waveneuro.databinding.ActivityMfaBinding
import com.waveneuro.ui.base.activity.BaseViewModelActivity
import com.waveneuro.ui.dashboard.DashboardActivity
import com.waveneuro.ui.user.mfa.viewmodel.MfaViewModel
import com.waveneuro.ui.user.mfa.viewmodel.MfaViewModelImpl
import com.waveneuro.utils.ext.getAppComponent
import com.waveneuro.utils.ext.hideKeyboard
import com.waveneuro.utils.ext.toast

class MfaActivity : BaseViewModelActivity<ActivityMfaBinding, MfaViewModel>() {

    override val viewModel: MfaViewModelImpl by viewModels {
        getAppComponent().mfaViewModelFactory()
    }

    var username: String? = ""
    var session: String? = ""
    var currentFocusedPosition = 0
    val viewsList = mutableListOf<TextInputEditText>()

    override fun initBinding(): ActivityMfaBinding = ActivityMfaBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
        super.onCreate(savedInstanceState)

        if (intent.hasExtra(SESSION))
            session = intent.getStringExtra(SESSION)

        if (intent.hasExtra(USERNAME))
            username = intent.getStringExtra(USERNAME)

        setView()
        initObservers()
    }

    private fun setView() {
        with(binding) {
            tvBackToLogin.setOnClickListener {
                onBackPressed()
            }
            viewsList.add(tipCode1)
            viewsList.add(tipCode2)
            viewsList.add(tipCode3)
            viewsList.add(tipCode4)
            viewsList.add(tipCode5)
            viewsList.add(tipCode6)
        }
        for (i in viewsList.indices) {
            viewsList[i].addTextChangedListener(mTextEditorWatcher)
            viewsList[i].onFocusChangeListener = View.OnFocusChangeListener { v, _ ->
                currentFocusedPosition = viewsList.indexOf(v)
            }
        }

        viewsList[0].requestFocus()

        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun initObservers() {
        viewModel.viewEffect.observe(this, Observer { viewEffect ->
            when (viewEffect) {
                is MfaViewEffect.Home -> launchHomeScreen()
                is MfaViewEffect.WrongMfaCode -> toast("The code you entered doesn’t match")
            }
        })
    }

    private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.length == 1) {
                if (currentFocusedPosition == 5) {
                    viewModel.processEvent(MfaViewEvent.ConfirmToken(buildMfaCode(), username, session))
                } else {
                    viewsList[currentFocusedPosition + 1].requestFocus()
                }
            } else if (s.isEmpty()) {
                if (currentFocusedPosition != 0) {
                    viewsList[currentFocusedPosition - 1].requestFocus()
                }
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }

    private fun buildMfaCode(): String {
        val mfaCode = StringBuffer()
        for (i in viewsList.indices) {
            mfaCode.append(viewsList[i].text.toString())
        }
        return mfaCode.toString()
    }

    private fun launchHomeScreen() {
        binding.root.hideKeyboard()
        startActivity(DashboardActivity.newIntent(this))
        finish()
    }

    companion object {
        private const val USERNAME = "username"
        private const val SESSION = "session"

        fun newIntent(context: Context, username: String?, session: String?) =
            Intent(context, MfaActivity::class.java).apply {
                putExtra(USERNAME, username)
                putExtra(SESSION, session)
            }
    }
}