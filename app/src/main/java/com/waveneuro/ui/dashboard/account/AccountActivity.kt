package com.waveneuro.ui.dashboard.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.waveneuro.R
import com.waveneuro.databinding.ActivityAccountBinding
import com.waveneuro.domain.model.user.UserInfo
import com.waveneuro.ui.base.activity.BaseViewModelActivity
import com.waveneuro.ui.dashboard.account.AccountViewEffect.*
import com.waveneuro.ui.dashboard.account.viewmodel.AccountViewModel
import com.waveneuro.ui.dashboard.account.viewmodel.AccountViewModelImpl
import com.waveneuro.ui.dashboard.organization.OrganizationActivity
import com.waveneuro.utils.ext.getAppComponent
import com.waveneuro.utils.ext.toast

class AccountActivity : BaseViewModelActivity<ActivityAccountBinding, AccountViewModel>() {

    override val viewModel: AccountViewModelImpl by viewModels {
        getAppComponent().accountViewModelFactory()
    }

    override fun initBinding(): ActivityAccountBinding =
        ActivityAccountBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setView()
        setObserver()
        viewModel.processEvent(AccountViewEvent.Start)
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
                viewModel.processEvent(AccountViewEvent.BackClicked)
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
                startActivity(OrganizationActivity.newIntent(this@AccountActivity))
            }
        }
    }

    private fun setObserver() {
        viewModel.viewEffect.observe(this, Observer { viewEffect ->
            when (viewEffect) {
                is BackRedirect -> goBack()
                is GetSuccess -> setValues(viewEffect.user)
                is UpdateSuccess -> {
                    setValues(viewEffect.user)
                    toast("Profile updated successfully.")
                }
            }
        })
    }

    private fun setValues(user: UserInfo) {
        with(binding) {
            tvFirstNameValue.text = user.firstName
            tvLastNameValue.text = user.lastName
            tvUserRoleValue.text = when (user.role) {
                "STORE_ADMIN" -> "Store Admin"
                "WN_ADMIN" -> "WN Admin"
                "STORE_USER" -> "Store User"
                "EEG_LAB_USER" -> "EEG Lab User"
                "WN_SALES" -> "WN Sales"
                "EXECUTIVE" -> "Executive"
                "CLIENT" -> "Client"
                else -> "Unknown"
            }
            tvEmailValue.text = user.email
            tvOrganizationValue.text = user.organizations.size.toString()
        }
    }

    private fun goBack() {
        onBackPressed()
    }

    private fun submit() {
        viewModel.processEvent(
            AccountViewEvent.UpdatedUser(
                binding.tvFirstNameValue.text.toString().trim(),
                binding.tvLastNameValue.text.toString().trim()
            )
        )
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, AccountActivity::class.java)
    }

}