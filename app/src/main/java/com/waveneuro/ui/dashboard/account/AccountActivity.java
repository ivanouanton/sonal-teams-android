package com.waveneuro.ui.dashboard.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
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
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textview.MaterialTextView;
import com.waveneuro.R;
import com.waveneuro.data.model.response.user.UserInfoResponse;
import com.waveneuro.ui.base.BaseFormActivity;
import com.waveneuro.utils.DateUtil;

import java.util.Date;

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

    @BindView(R.id.tvUsernameValue)
    TextView tvUsername;

    @BindView(R.id.llUsername)
    RelativeLayout llUsername;

    @BindView(R.id.tvBirthdayValue)
    TextView tvBirthday;

    @BindView(R.id.llBirthday)
    RelativeLayout llBirthday;

    @BindView(R.id.tvEmailValue)
    TextView tvEmail;

    @BindView(R.id.llEmail)
    RelativeLayout llEmail;

    @BindView(R.id.tvOrganizationValue)
    TextView tvOrganization;

    @BindView(R.id.tvChiefValue)
    TextView tvChiefComplaint;

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
            tvFirstName.setText(userInfoResponse.getGivenName());
            tvLastName.setText(userInfoResponse.getFamilyName());
            tvUsername.setText(userInfoResponse.getUsername());
            tvBirthday.setText(DateUtil.parseDate(userInfoResponse.getBirthdate(),
                    DateUtil.PATTERN_RFC1123,
                    DateUtil.PATTERN_ISO_DATE));
            tvEmail.setText(userInfoResponse.getEmail());
            tvOrganization.setText(userInfoResponse.getLocation());
            tvChiefComplaint.setText(userInfoResponse.getCustomGoal());

            if (TextUtils.isEmpty(userInfoResponse.getImageThumbnailUrl())) {
                if (TextUtils.isEmpty(userInfoResponse.getName())) {
                    tvProfileImageInitials.setText("");
                    return;
                }
                String[] strArray = userInfoResponse.getName().split(" ");
                StringBuilder builder = new StringBuilder();
                if (strArray.length > 0) {
                    builder.append(strArray[0], 0, 1);
                }
                if (strArray.length > 1) {
                    builder.append(strArray[1], 0, 1);
                }
//                if (strArray.length > 2) {
//                    builder.append(strArray[2], 0, 1);
//                }
                tvProfileImageInitials.setText(builder.toString().toUpperCase());
                ivProfileImage.setVisibility(View.VISIBLE);
                tvProfileImageInitials.setVisibility(View.VISIBLE);
            } else {
                ivProfileImage.setVisibility(View.VISIBLE);
                // TODO Visible when Image is loaded
                Glide.with(this)
                        .load(userInfoResponse.getImageThumbnailUrl())
                        .into(ivProfileImage);
            }
        }
    }

    public void openDataPicker() {
        Date dob = DateUtil.parseDate(tvBirthday.getText().toString().trim(), DateUtil.PATTERN_ISO_DATE);
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(dob != null ? dob.getTime() : MaterialDatePicker.todayInUtcMilliseconds())
                .setTitleText("Select Birth date").build();

        datePicker.show(getSupportFragmentManager(), datePicker.toString());
        datePicker.addOnPositiveButtonClickListener(selection -> {
            String dateString = DateFormat.format(DateUtil.PATTERN_ISO_DATE, new Date((Long) selection)).toString();
            tvBirthday.setText(dateString);
            submit();
        });
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

    @OnClick(R.id.llBirthday)
    public void onBirthdayClicked() {
        openDataPicker();
    }

    @OnClick(R.id.llEmail)
    public void onEmailClicked() {
        buildDialog(R.string.enter_email, tvEmail).show();
    }

    @OnClick(R.id.llUsername)
    public void onUsernameClicked() {
        buildDialog(R.string.enter_username, tvUsername).show();
    }

    @Override
    public void submit() {
        String dateString = DateUtil.parseDate(tvBirthday.getText().toString().trim(), DateUtil.PATTERN_ISO_DATE, "MM/dd/yyyy");
        this.accountViewModel.processEvent(
                new AccountViewEvent.UpdatedUser(
                        tvFirstName.getText().toString().trim(),
                        tvLastName.getText().toString().trim(),
                        tvUsername.getText().toString().trim(),
                        tvEmail.getText().toString().trim(),
                        dateString
                ));
    }
}