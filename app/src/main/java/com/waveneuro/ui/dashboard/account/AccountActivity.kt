package com.waveneuro.ui.dashboard.account

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.asif.abase.data.model.BaseModel
import com.waveneuro.R
import com.waveneuro.data.model.response.user.UserInfoResponse
import com.waveneuro.databinding.ActivityAccountBinding
import com.waveneuro.ui.base.BaseFormActivity
import com.waveneuro.ui.dashboard.account.AccountViewEffect.BackRedirect
import com.waveneuro.ui.dashboard.account.AccountViewEffect.UpdateSuccess
import com.waveneuro.ui.dashboard.account.AccountViewState.*
import com.waveneuro.ui.dashboard.organization.OrganizationCommand
import com.waveneuro.utils.ext.toast
import javax.inject.Inject

class AccountActivity : BaseFormActivity() {

    private lateinit var binding: ActivityAccountBinding

    @Inject
    lateinit var accountViewModel: AccountViewModel
    @Inject
    lateinit var organizationCommand: OrganizationCommand

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent()?.inject(this)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setView()
        setObserver()
        accountViewModel.processEvent(AccountViewEvent.Start)
    }

    private fun buildDialog(@StringRes title: Int, textView: TextView?): AlertDialog {
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle(title)
        val input = EditText(this)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.setPadding(80, 40, 80, 40)
        input.layoutParams = lp
        input.setText(textView?.text)
        alertDialog.setView(input)
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.btn_save)) { _, _ ->
            textView?.text = input.text
            submit()
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.btn_cancel)) { _, _ ->
            alertDialog.dismiss()
        }
        return alertDialog
    }

    private fun setView() {
        with(binding) {
            ivBack.setOnClickListener {
                accountViewModel.processEvent(AccountViewEvent.BackClicked)
            }
            llFirstName.setOnClickListener {
                buildDialog(R.string.enter_first_name, tvFirstNameValue).show()
            }
            llLastName.setOnClickListener {
                buildDialog(R.string.enter_last_name, tvLastNameValue).show()
            }
            llEmail.setOnClickListener {
                buildDialog(R.string.enter_email, tvEmailValue).show()
            }
            llOrganization.setOnClickListener {
                organizationCommand.navigate()
            }
        }
    }

    private fun setObserver() {
        accountViewModel.data.observe(this, Observer { viewState ->
            when (viewState) {
                is Success -> onSuccess(viewState.item)
                is Failure -> onFailure(viewState.error)
                is Loading -> {
                    if (viewState.loading) displayWait("Loading...", null)
                    else removeWait()
                }
                else -> {}
            }
        })
        accountViewModel.viewEffect.observe(this, Observer { viewEffect: AccountViewEffect? ->
            when (viewEffect) {
                is BackRedirect -> goBack()
                is UpdateSuccess -> toast("Profile updated successfully.")
                else -> {}
            }
        })
    }

    override fun onSuccess(model: BaseModel) {
        super.onSuccess(model)
        if (model is UserInfoResponse) {
            with(binding) {
                tvFirstNameValue.text = model.firstName
                tvLastNameValue.text = model.lastName
                tvUserRoleValue.text = when (model.role) {
                    "STORE_ADMIN" -> "Store Admin"
                    "WN_ADMIN" -> "WN Admin"
                    "STORE_USER" -> "Store User"
                    "EEG_LAB_USER" -> "EEG Lab User"
                    "WN_SALES" -> "WN Sales"
                    "EXECUTIVE" -> "Executive"
                    "CLIENT" -> "Client"
                    else -> "Unknown"
                }
                tvEmailValue.text = model.email
                tvOrganizationValue.text = model.organizations.size.toString()
                //TODO where is image url?
//                if (!TextUtils.isEmpty(model.imageThumbnailUrl)) {
//                    ivProfileImage.visibility = View.VISIBLE
//                    Glide.with(this@AccountActivity)
//                        .load(model.imageThumbnailUrl)
//                        .into(ivProfileImage)
//                } else {
//                    ivProfileImage.visibility = View.INVISIBLE
//                }
            }
        }
    }

    private fun goBack() {
        onBackPressed()
    }

    override fun submit() {
        accountViewModel.processEvent(
            AccountViewEvent.UpdatedUser(
                binding.tvFirstNameValue.text.toString().trim(),
                binding.tvLastNameValue.text.toString().trim()
            )
        )
    }

}