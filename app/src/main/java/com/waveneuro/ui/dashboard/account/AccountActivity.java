package com.waveneuro.ui.dashboard.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;

import com.asif.abase.data.model.BaseModel;
import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.waveneuro.R;
import com.waveneuro.data.model.response.user.UserInfoResponse;
import com.waveneuro.ui.base.BaseFormActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends BaseFormActivity {

    @BindView(R.id.iv_profile_image)
    ImageView ivProfileImage;

    @BindView(R.id.tv_profile_image_initials)
    MaterialTextView tvProfileImageInitials;

    @BindView(R.id.tvFirstNameValue)
    TextView tvFirstName;

    @BindView(R.id.llFirstName)
    RelativeLayout llFirstName;

    @BindView(R.id.tvLastNameValue)
    TextView tvLastName;

    @BindView(R.id.llLastName)
    RelativeLayout llLastName;

    @BindView(R.id.tvUserRoleValue)
    TextView tvUserRole;

    @BindView(R.id.llUserRole)
    RelativeLayout llUsername;

    @BindView(R.id.tvEmailValue)
    TextView tvEmail;

    @BindView(R.id.llEmail)
    RelativeLayout llEmail;

    @BindView(R.id.tvOrganizationValue)
    TextView tvOrganization;

    @Inject
    AccountViewModel accountViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        setView();
        setObserver();

        this.accountViewModel.processEvent(new AccountViewEvent.Start());
    }

    private AlertDialog buildDialog(@StringRes int title, TextView textView){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);

        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setPadding(80, 40,80,40);
        input.setLayoutParams(lp);
        input.setText(textView.getText());
        alertDialog.setView(input);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.btn_save), (dialog, which) -> {
            textView.setText(input.getText());
            submit();
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.btn_cancel), (dialog, which) -> {
            alertDialog.dismiss();
        });
        return alertDialog;
    }

    private void setView() {

    }

    private void setObserver() {
        this.accountViewModel.getData().observe(this, accountViewStateObserver);
        this.accountViewModel.getViewEffect().observe(this, accountViewEffectObserver);
    }

    Observer<AccountViewState> accountViewStateObserver = viewState -> {
        if (viewState instanceof AccountViewState.Success) {
            AccountViewState.Success success = (AccountViewState.Success) viewState;
            onSuccess(success.getItem());
        } else if (viewState instanceof AccountViewState.Failure) {
            AccountViewState.Failure error = (AccountViewState.Failure) viewState;
            onFailure(error.getError());
        } else if (viewState instanceof AccountViewState.Loading) {
            AccountViewState.Loading loading = ((AccountViewState.Loading) viewState);
            if (loading.getLoading())
                displayWait("Loading...", null);
            else
                removeWait();
        }
    };

    Observer<AccountViewEffect> accountViewEffectObserver = viewEffect -> {
        if (viewEffect instanceof AccountViewEffect.BackRedirect) {
            goBack();
        } else if (viewEffect instanceof AccountViewEffect.UpdateSuccess) {
            Toast.makeText(AccountActivity.this, "Profile updated successfully.",
                    Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onSuccess(BaseModel model) {
        super.onSuccess(model);
        if (model instanceof UserInfoResponse) {
            UserInfoResponse userInfoResponse = (UserInfoResponse) model;
            tvFirstName.setText(userInfoResponse.getFirstName());
            tvLastName.setText(userInfoResponse.getLastName());
            if (userInfoResponse.getRole().equals("STORE_ADMIN")) {
                tvUserRole.setText("Store Admin");
            } else if (userInfoResponse.getRole().equals("WN_ADMIN")) {
                tvUserRole.setText("WN Admin");
            } else if (userInfoResponse.getRole().equals("STORE_USER")) {
                tvUserRole.setText("Store User");
            } else if (userInfoResponse.getRole().equals("EEG_LAB_USER")) {
                tvUserRole.setText("EEG Lab User");
            } else if (userInfoResponse.getRole().equals("WN_SALES")) {
                tvUserRole.setText("WN Sales");
            } else if (userInfoResponse.getRole().equals("EXECUTIVE")) {
                tvUserRole.setText("Executive");
            } else if (userInfoResponse.getRole().equals("CLIENT")) {
                tvUserRole.setText("Client");
            } else {
                tvUserRole.setText("Unknown");
            }
            tvEmail.setText(userInfoResponse.getEmail());
            tvOrganization.setText(userInfoResponse.getOrganizationName());

            if (TextUtils.isEmpty(userInfoResponse.getImageThumbnailUrl())) {

            } else {
                ivProfileImage.setVisibility(View.VISIBLE);
                // TODO Visible when Image is loaded
                Glide.with(this)
                        .load(userInfoResponse.getImageThumbnailUrl())
                        .into(ivProfileImage);
            }
        }
    }


    private void goBack() {
        onBackPressed();
    }

    @OnClick(R.id.iv_back)
    public void onBackClicked() {
        this.accountViewModel.processEvent(new AccountViewEvent.BackClicked());
    }

    @OnClick(R.id.llFirstName)
    public void onFirstNameClicked() {
        buildDialog(R.string.enter_first_name, tvFirstName).show();
    }

    @OnClick(R.id.llLastName)
    public void onLastNameClicked() {
        buildDialog(R.string.enter_last_name, tvLastName).show();
    }

    @OnClick(R.id.llEmail)
    public void onEmailClicked() {
        buildDialog(R.string.enter_email, tvEmail).show();
    }

    @Override
    public void submit() {
        this.accountViewModel.processEvent(
                new AccountViewEvent.UpdatedUser(
                        tvFirstName.getText().toString().trim(),
                        tvLastName.getText().toString().trim()
                ));
    }
}