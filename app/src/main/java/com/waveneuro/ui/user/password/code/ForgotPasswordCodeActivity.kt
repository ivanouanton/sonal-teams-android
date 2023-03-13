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
import com.waveneuro.ui.dashboard.DashboardCommand
import com.waveneuro.ui.dashboard.web.WebCommand
import com.waveneuro.ui.user.login.LoginActivity
import com.waveneuro.ui.user.password.code.viewmodel.ForgotPasswordCodeViewModel
import com.waveneuro.ui.user.password.code.viewmodel.ForgotPasswordCodeViewModelImpl
import com.waveneuro.ui.user.password.new_password.SetNewPasswordCommand
import com.waveneuro.utils.ext.getAppComponent
import com.waveneuro.utils.ext.toast
import javax.inject.Inject

internal class ForgotPasswordCodeActivity :
    BaseViewModelActivity<ActivityForgotPasswordCodeBinding, ForgotPasswordCodeViewModel>() {

    var username: String? = ""
    var session = ""
    var viewsList: ArrayList<TextInputEditText>? = null
    var currentFocusedPosition = 1

    @Inject
    var dashboardCommand: DashboardCommand? = null
    @Inject
    var setNewPasswordCommand: SetNewPasswordCommand? = null
    @Inject
    var webCommand: WebCommand? = null

    override val viewModel: ForgotPasswordCodeViewModelImpl by viewModels {
        getAppComponent().forgotPasswordCodeViewModelFactory()
    }

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
        viewsList = ArrayList()
        viewsList!!.add(findViewById(R.id.tip_code1))
        viewsList!!.add(findViewById(R.id.tip_code2))
        viewsList!!.add(findViewById(R.id.tip_code3))
        viewsList!!.add(findViewById(R.id.tip_code4))
        viewsList!!.add(findViewById(R.id.tip_code5))
        viewsList!!.add(findViewById(R.id.tip_code6))
        for (i in viewsList!!.indices) {
            viewsList!![i].addTextChangedListener(mTextEditorWatcher)
            viewsList!![i].setOnKeyListener(onKeyListener)
        }
        viewsList!![0].requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        setView()
        setObservers()
    }

    private fun setView() {
        with(binding) {
            tvResendCode.setOnClickListener {
                viewModel.resetPassword(username)
            }
            tvLogIn.setOnClickListener {
                startActivity(LoginActivity.newIntent(this@ForgotPasswordCodeActivity))
            }
            tvSupport.setOnClickListener {
                webCommand!!.navigate(WebCommand.PAGE_SUPPORT)
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
                    viewsList!![currentFocusedPosition].requestFocus()
                    currentFocusedPosition++
                } else {
                    setNewPasswordCommand!!.navigate(username, buildCode())
                }
            }
        }

        override fun afterTextChanged(s: Editable) {
            val x = 0
        }
    }

    private fun buildCode(): String {
        val mfaCode = StringBuffer()
        for (i in viewsList!!.indices) {
            mfaCode.append(viewsList!![i].text.toString())
        }
        return mfaCode.toString()
    }

    private val onKeyListener =
        View.OnKeyListener { view, i, keyEvent -> //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
            if (i == KeyEvent.KEYCODE_DEL) {
                if (currentFocusedPosition > 1) {
                    viewsList!![currentFocusedPosition - 2].requestFocus()
                    currentFocusedPosition--
                }
            }
            false
        }


    private fun launchHomeScreen() {
        dashboardCommand!!.navigate()
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
//
//public class ForgotPasswordCodeActivity extends BaseFormActivity {
//
//    String username = "";
//    String session = "";
//
//    ArrayList<TextInputEditText> viewsList = null;
//
//    int currentFocusedPosition = 1;
//
//    @Inject
//    DashboardCommand dashboardCommand;
//    @Inject
//    SetNewPasswordCommand setNewPasswordCommand;
//    @Inject
//    WebCommand webCommand;
//    @Inject
//    LoginCommand loginCommand;
//
//    @BindView(R.id.tv_log_in)
//    TextView tvLogIn;
//
//    @BindView(R.id.tv_resend_code)
//    TextView tvResendCode;
//
//    @BindView(R.id.tv_support)
//    TextView tvSupport;
//
//
//    @Inject
//    ForgotPasswordCodeViewModelImpl forgotPasswordCodeViewModelImpl;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
//
//        super.onCreate(savedInstanceState);
//        activityComponent().inject(this);
//
//        setContentView(R.layout.activity_forgot_password_code);
//        ButterKnife.bind(this);
//
//        if(getIntent().hasExtra(ForgotPasswordCodeCommand.EMAIL)) {
//            username = getIntent().getStringExtra(ForgotPasswordCodeCommand.EMAIL);
//        }
//
//        viewsList = new ArrayList<>();
//        viewsList.add(findViewById(R.id.tip_code1));
//        viewsList.add(findViewById(R.id.tip_code2));
//        viewsList.add(findViewById(R.id.tip_code3));
//        viewsList.add(findViewById(R.id.tip_code4));
//        viewsList.add(findViewById(R.id.tip_code5));
//        viewsList.add(findViewById(R.id.tip_code6));
//
//        for (int i = 0; i < viewsList.size(); i++){
//        viewsList.get(i).addTextChangedListener(mTextEditorWatcher);
//        viewsList.get(i).setOnKeyListener(onKeyListener);
//    }
//
//        viewsList.get(0).requestFocus();
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//
//        //this.loginViewModel.getViewEffect().observe(this, loginViewEffectObserver);
//        this.forgotPasswordCodeViewModelImpl.getViewEffect().observe(this, loginViewEffectObserver);
//
//        setView();
//    }
//
//    private void setView() {
//        logInSpanText();
//        supportSpanText();
//        resendSpanText();
//
//    }
//
//    private void logInSpanText() {
//        SpannableString spannableString = new SpannableString(getString(R.string.log_in));
//        spannableString.setSpan(new UnderlineSpan(), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_dim)), 0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        tvLogIn.setText(spannableString);
//    }
//
//    private void supportSpanText() {
//        SpannableString spannableString = new SpannableString(getString(R.string.support));
//        spannableString.setSpan(new UnderlineSpan(), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_dim)), 0, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        tvSupport.setText(spannableString);
//    }
//
//    private void resendSpanText() {
//        SpannableString spannableString = new SpannableString(getString(R.string.resend_code));
//        spannableString.setSpan(new UnderlineSpan(), 0, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_dim)), 0, 11, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        tvResendCode.setText(spannableString);
//    }
//
//    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        }
//
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if (s.length() == 1) {
//                if (currentFocusedPosition < 6) {
//                    viewsList.get(currentFocusedPosition).requestFocus();
//                    currentFocusedPosition++;
//                } else {
//                    setNewPasswordCommand.navigate(username, buildCode());
//                }
//            }
//        }
//
//        public void afterTextChanged(Editable s) {
//            int x = 0;
//        }
//    };
//
//    private String buildCode(){
//        StringBuffer mfaCode = new StringBuffer();
//        for (int i = 0; i < viewsList.size(); i++){
//        mfaCode.append(viewsList.get(i).getText().toString());
//    }
//        return mfaCode.toString();
//    }
//
//    private final View.OnKeyListener onKeyListener  = new View.OnKeyListener() {
//        @Override
//        public boolean onKey(View view, int i, KeyEvent keyEvent) {
//            //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
//            if(i == KeyEvent.KEYCODE_DEL) {
//                if (currentFocusedPosition > 1) {
//                    viewsList.get(currentFocusedPosition - 2).requestFocus();
//                    currentFocusedPosition--;
//                }
//            }
//            return false;
//        }
//    };
//
//    Observer<LoginViewEffect> loginViewEffectObserver = viewEffect -> {
//        if (viewEffect instanceof LoginViewEffect.Home) {
//            launchHomeScreen();
//        } else if (viewEffect instanceof LoginViewEffect.WrongMfaCode) {
//            Toast.makeText(this, "Wrong code", Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    private void launchHomeScreen() {
//        dashboardCommand.navigate();
//    }
//
//    @Override
//    public void submit() {
//
//    }
//
//    @OnClick(R.id.tv_resend_code)
//    public void onClickResendCode() {
//        forgotPasswordCodeViewModelImpl.resetPassword(username);
//
//    }
//
//    @OnClick(R.id.tv_log_in)
//    public void onClickLogIn() {
//        loginCommand.navigate();
//
//    }
//
//    @OnClick({R.id.tv_support})
//    public void onClickSupport() {
//        webCommand.navigate(WebCommand.PAGE_SUPPORT);
//    }
}