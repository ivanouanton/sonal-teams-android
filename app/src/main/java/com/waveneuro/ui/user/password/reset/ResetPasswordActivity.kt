package com.waveneuro.ui.user.password.reset

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import butterknife.BindView
import butterknife.OnClick
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.waveneuro.R
import com.waveneuro.databinding.ActivityResetPasswordBinding
import com.waveneuro.ui.base.activity.BaseViewModelActivity
import com.waveneuro.ui.dashboard.web.WebCommand
import com.waveneuro.ui.user.login.LoginCommand
import com.waveneuro.ui.user.login.viewmodel.LoginViewModelImpl
import com.waveneuro.ui.user.password.code.ForgotPasswordCodeCommand
import com.waveneuro.ui.user.password.reset.ResetPasswordViewEffect.LoginRedirect
import com.waveneuro.ui.user.password.reset.viewmodel.ResetPasswordViewModel
import com.waveneuro.ui.user.password.reset.viewmodel.ResetPasswordViewModelImpl
import com.waveneuro.utils.ext.getAppComponent
import javax.inject.Inject

class ResetPasswordActivity : BaseViewModelActivity<ActivityResetPasswordBinding, ResetPasswordViewModel>() {

    @NotEmpty(trim = true, message = "Enter email")
    @BindView(R.id.et_username)
    var etUsername: TextInputLayout? = null

    @JvmField
    @BindView(R.id.tv_log_in)
    var tvLogIn: TextView? = null

    @JvmField
    @BindView(R.id.btn_send_reset_link)
    var btnSendLink: MaterialButton? = null

    @JvmField
    @Inject
    var forgotPasswordCodeCommand: ForgotPasswordCodeCommand? = null

    @JvmField
    @Inject
    var loginCommand: LoginCommand? = null

    @JvmField
    @Inject
    var webCommand: WebCommand? = null

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
        tvLogIn!!.text = spannableString
    }

    private fun setupInputWatcher() {
        etUsername!!.editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                btnSendLink!!.isEnabled = count != 0
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun setObserver() {
        viewModel.getData().observe(this, resetPasswordViewStateObserver)
        viewModel.getViewEffect().observe(this, resetPasswordViewEffectObserver)
    }

    var resetPasswordViewStateObserver = Observer { viewState: ResetPasswordViewState? ->
        when (viewState) {
            is ResetPasswordViewState.Success -> {
                launchCheckEmailDialog()
            }
            is ResetPasswordViewState.Failure -> {
                val error = viewState
                if (Integer.valueOf(error.error.code) == 400) {
                    val view = findViewById<View>(R.id.rpa_root)
                    val snackBar =
                        Snackbar.make(view, R.string.email_does_not_match, Snackbar.LENGTH_LONG)
                    snackBar.duration = 100000000
                    snackBar.setAction(R.string.ok) { snackBar.dismiss() }
                    snackBar.show()
                } else {
                    onFailure(error.error)
                }
            }
            is ResetPasswordViewState.Loading -> {
                if (viewState.loading) displayWait("Loading...", null) else removeWait()
            }
        }
    }
    var resetPasswordViewEffectObserver = Observer { viewEffect: ResetPasswordViewEffect? ->
        if (viewEffect is ResetPasswordViewEffect.BackRedirect) {
            goBack()
        } else if (viewEffect is LoginRedirect) {
            launchLoginScreen()
        }
    }

    private fun launchLoginScreen() {
        loginCommand!!.navigate()
        finish()
    }

    private fun launchCheckEmailDialog() {
        val builder = MaterialAlertDialogBuilder(this, R.style.PopUp)
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_popup, viewGroup, false)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tv_title)
        val tvContent = dialogView.findViewById<TextView>(R.id.tv_content)
        val btnClose = dialogView.findViewById<ImageButton>(R.id.ibtn_close)
        btnClose.visibility = View.VISIBLE
        val btnPrimary = dialogView.findViewById<Button>(R.id.btn_primary)
        tvTitle.setText(R.string.check_your_email)
        tvContent.setText(R.string.recovery_info)
        btnPrimary.setText(R.string.open_email_app)
        builder.setView(dialogView)
        val ad = builder.create()
        btnPrimary.setOnClickListener { v: View? ->
            ad.dismiss()
            forgotPasswordCodeCommand!!.navigate(
                etUsername!!.editText!!.text.toString().trim { it <= ' ' })
            try {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_APP_EMAIL)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                this.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
            }
        }
        btnClose.setOnClickListener {
            ad.dismiss()
            forgotPasswordCodeCommand!!.navigate(
                etUsername!!.editText!!.text.toString().trim()
            )
        }
        ad.show()
    }

    @OnClick(R.id.tv_log_in)
    fun onClickBack() {
        loginCommand!!.navigate()
    }

    private fun goBack() {
        onBackPressed()
    }

    override fun submit() {
        viewModel.processEvent(
            ResetPasswordViewEvent.ForgotPasswordClicked(
                etUsername!!.editText!!.text.toString().trim()
        )
    )
    }

    @OnClick(R.id.btn_send_reset_link)
    fun onClickSendResetLink() {
        mValidator.validate()
    }
}

//public class ResetPasswordActivity extends BaseFormActivity {
//
//    @NotEmpty(trim = true, message = "Enter email")
//    @BindView(R.id.et_username)
//    TextInputLayout etUsername;
//
//    @BindView(R.id.tv_log_in)
//    TextView tvLogIn;
//
//    @BindView(R.id.btn_send_reset_link)
//    MaterialButton btnSendLink;
//
//    @Inject
//    ResetPasswordViewModel viewModel;
//
//    @Inject
//    RegistrationCommand registrationCommand;
//    @Inject
//    ForgotPasswordCodeCommand forgotPasswordCodeCommand;
//
//    @Inject
//    LoginCommand loginCommand;
//
//    @Inject
//    WebCommand webCommand;
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
//        setContentView(R.layout.activity_reset_password);
//        ButterKnife.bind(this);
//
//        setView();
//        setObserver();
//    }
//
//    private void setView() {
//        logInSpanText();
//        setupInputWatcher();
//    }
//
//    private void logInSpanText() {
//        SpannableString spannableString = new SpannableString(getString(R.string.log_in));
//
//        spannableString.setSpan(new UnderlineSpan(), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_dim)), 0, 6, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        tvLogIn.setText(spannableString);
//    }
//
//    private void setupInputWatcher() {
//        etUsername.getEditText().addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                btnSendLink.setEnabled(count != 0);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//    }
//
//    private void setObserver() {
//        this.viewModel.getData().observe(this, resetPasswordViewStateObserver);
//        this.viewModel.getViewEffect().observe(this, resetPasswordViewEffectObserver);
//    }
//
//    Observer<ResetPasswordViewState> resetPasswordViewStateObserver = viewState -> {
//        if (viewState instanceof ResetPasswordViewState.Success) {
//            ResetPasswordViewState.Success success = (ResetPasswordViewState.Success) viewState;
//            launchCheckEmailDialog();
//        } else if (viewState instanceof ResetPasswordViewState.Failure) {
//            ResetPasswordViewState.Failure error = (ResetPasswordViewState.Failure) viewState;
//
//            if (Integer.valueOf(error.getError().getCode()) == 400) {
//                View view = findViewById(R.id.rpa_root);
//                final Snackbar snackBar = Snackbar.make(view, R.string.email_does_not_match, Snackbar.LENGTH_LONG);
//                snackBar.setDuration(100000000);
//                snackBar.setAction(R.string.ok, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        snackBar.dismiss();
//                    }
//                });
//                snackBar.show();
//            } else {
//                onFailure(error.getError());
//            }
//
//        } else if (viewState instanceof ResetPasswordViewState.Loading) {
//            ResetPasswordViewState.Loading loading = ((ResetPasswordViewState.Loading) viewState);
//            if (loading.getLoading())
//                displayWait("Loading...", null);
//            else
//                removeWait();
//        }
//    };
//
//    Observer<ResetPasswordViewEffect> resetPasswordViewEffectObserver = viewEffect -> {
//        if (viewEffect instanceof ResetPasswordViewEffect.BackRedirect) {
//            goBack();
//        } else if (viewEffect instanceof ResetPasswordViewEffect.LoginRedirect) {
//            launchLoginScreen();
//        }
//    };
//
//    private void launchLoginScreen() {
//        loginCommand.navigate();
//        finish();
//    }
//
//    private void launchCheckEmailDialog() {
//        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this, R.style.PopUp);
//        ViewGroup viewGroup = findViewById(android.R.id.content);
//        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_popup, viewGroup, false);
//        TextView tvTitle = dialogView.findViewById(R.id.tv_title);
//        TextView tvContent = dialogView.findViewById(R.id.tv_content);
//        ImageButton btnClose = dialogView.findViewById(R.id.ibtn_close);
//        btnClose.setVisibility(View.VISIBLE);
//        Button btnPrimary = dialogView.findViewById(R.id.btn_primary);
//        tvTitle.setText(R.string.check_your_email);
//        tvContent.setText(R.string.recovery_info);
//        btnPrimary.setText(R.string.open_email_app);
//        builder.setView(dialogView);
//        final AlertDialog ad = builder.create();
//        btnPrimary.setOnClickListener(v -> {
//        ad.dismiss();
//        forgotPasswordCodeCommand.navigate(etUsername.getEditText().getText().toString().trim());
//        try {
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            this.startActivity(intent);
//        } catch (android.content.ActivityNotFoundException e) {
//    }
//    });
//        btnClose.setOnClickListener(v -> {
//        ad.dismiss();
//        forgotPasswordCodeCommand.navigate(etUsername.getEditText().getText().toString().trim());
//    });
//        ad.show();
//    }
//
//    @OnClick(R.id.tv_log_in)
//    public void onClickBack() {
//        loginCommand.navigate();
//    }
//
//    private void goBack() {
//        onBackPressed();
//    }
//
//    @Override
//    public void submit() {
//        this.viewModel.processEvent(
//            new ResetPasswordViewEvent.ForgotPasswordClicked(
//                    etUsername.getEditText().getText().toString().trim()));
//    }
//
//    @OnClick(R.id.btn_send_reset_link)
//    public void onClickSendResetLink() {
//        this.mValidator.validate();
//    }
//
//}