package com.waveneuro.ui.dashboard.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.lifecycle.Observer;

import com.asif.abase.data.model.BaseModel;
import com.bumptech.glide.Glide;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
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

    @NotEmpty(message = "First name is mandatory")
    @BindView(R.id.tip_first_name)
    TextInputEditText tipFirstName;

    @NotEmpty(message = "Last name is mandatory")
    @BindView(R.id.tip_last_name)
    TextInputEditText tipLastName;

    @NotEmpty(message = "Username is mandatory")
    @BindView(R.id.tip_username)
    TextInputEditText tipUsername;

    @NotEmpty(message = "Date of birth is mandatory")
    @BindView(R.id.tip_dob)
    TextInputEditText tipDob;

    @NotEmpty(message = "Email is mandatory")
    @Email(message = "Email is invalid")
    @BindView(R.id.tip_email)
    TextInputEditText tipEmail;

    @BindView(R.id.tv_organization)
    MaterialTextView tvOrganization;

    @BindView(R.id.tv_chief_complaint)
    MaterialTextView tvChiefComplaint;

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
            tipFirstName.setText(userInfoResponse.getGivenName());
            tipLastName.setText(userInfoResponse.getFamilyName());
            tipUsername.setText(userInfoResponse.getUsername());
            tipDob.setText(DateUtil.parseDate(userInfoResponse.getBirthdate(),
                    DateUtil.PATTERN_RFC1123,
                    DateUtil.PATTERN_ISO_DATE));
            tipEmail.setText(userInfoResponse.getEmail());
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

    public void openDataPicker(View view) {
        Date dob = DateUtil.parseDate(tipDob.getText().toString().trim(), DateUtil.PATTERN_ISO_DATE);
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(dob != null ? dob.getTime() : MaterialDatePicker.todayInUtcMilliseconds())
                .setTitleText("Select Birth date").build();

        datePicker.show(getSupportFragmentManager(), datePicker.toString());
        datePicker.addOnPositiveButtonClickListener(selection -> {
//                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//                calendar.setTimeInMillis((Long) selection);
//            String dateString = DateFormat.format("MM/dd/yyyy", new Date((Long) selection)).toString();
            String dateString = DateFormat.format(DateUtil.PATTERN_ISO_DATE, new Date((Long) selection)).toString();
            ((TextInputEditText) view).setText(dateString);
        });
    }

    private void goBack() {
        onBackPressed();
    }

    @OnClick(R.id.btn_save_changes)
    public void onClickSaveChanges() {
        this.mValidator.validate();
    }

    @OnClick(R.id.iv_back)
    public void onBackClicked() {
        this.accountViewModel.processEvent(new AccountViewEvent.BackClicked());
    }

    @Override
    public void submit() {
        String dateString = DateUtil.parseDate(tipDob.getText().toString().trim(), DateUtil.PATTERN_ISO_DATE, "MM/dd/yyyy");
        this.accountViewModel.processEvent(
                new AccountViewEvent.UpdatedUser(
                        tipFirstName.getText().toString().trim(),
                        tipLastName.getText().toString().trim(),
                        tipUsername.getText().toString().trim(),
                        tipEmail.getText().toString().trim(),
                        dateString
                ));
    }
}