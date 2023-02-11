package com.waveneuro.ui.user.mfa

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.waveneuro.databinding.ActivityMfaBinding
import com.waveneuro.ui.base.BaseActivity
import com.waveneuro.ui.dashboard.DashboardCommand
import com.waveneuro.ui.user.login.LoginViewEffect
import com.waveneuro.ui.user.login.LoginViewEffect.Home
import com.waveneuro.ui.user.login.LoginViewEffect.WrongMfaCode
import com.waveneuro.utils.ext.hideKeyboard
import javax.inject.Inject

class MfaActivity : BaseActivity() {

    @Inject
    lateinit var dashboardCommand: DashboardCommand
    @Inject
    lateinit var mfaViewModel: MfaViewModel

    private lateinit var binding: ActivityMfaBinding

    var username: String? = ""
    var session: String? = ""
    var currentFocusedPosition = 0
    val viewsList = mutableListOf<TextInputEditText>()

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
        super.onCreate(savedInstanceState)
        activityComponent()?.inject(this)
        binding = ActivityMfaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(MfaCommand.SESSION))
            session = intent.getStringExtra(MfaCommand.SESSION)

        if (intent.hasExtra(MfaCommand.USERNAME))
            username = intent.getStringExtra(MfaCommand.USERNAME)

        with(binding) {
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
        mfaViewModel.viewEffect.observe(this, loginViewEffectObserver)

        binding.tvBackToLogin.setOnClickListener {
            onBackPressed()
        }
    }

    private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.length == 1) {
                if (currentFocusedPosition == 5) {
                    mfaViewModel.confirmToken(buildMfaCode(), username, session)
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

    private val loginViewEffectObserver = Observer { viewEffect: LoginViewEffect? ->
        if (viewEffect is Home) {
            launchHomeScreen()
        } else if (viewEffect is WrongMfaCode) {
            Toast.makeText(this, "The code you entered doesn’t match", Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchHomeScreen() {
        binding.root.hideKeyboard()
        dashboardCommand.navigate()
    }
}