package com.waveneuro.ui.dashboard.home.bottom_sheet.view_client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.waveneuro.R
import com.waveneuro.data.model.response.common.TosStatus
import com.waveneuro.databinding.DialogViewClientBinding
import com.waveneuro.injection.component.DaggerFragmentComponent
import com.waveneuro.injection.component.FragmentComponent
import com.waveneuro.injection.module.FragmentModule
import com.waveneuro.ui.base.BaseActivity
import com.waveneuro.ui.dashboard.home.bottom_sheet.edit_client.EditClientBottomSheet
import com.waveneuro.ui.model.client.ClientUi
import com.waveneuro.ui.session.history.SessionHistoryCommand
import com.waveneuro.utils.DateHelper
import javax.inject.Inject

class ViewClientBottomSheet : BottomSheetDialogFragment() {

    @Inject
    lateinit var sessionHistoryCommand: SessionHistoryCommand

    private lateinit var fragmentComponent: FragmentComponent
    private lateinit var binding: DialogViewClientBinding

    private var clientId: Int = 0
    private var firstName: String? = null
    private var lastName: String? = null
    private var birthday: String? = null
    private var isMale = false
    private var email: String? = null
    private var username: String? = null
    private var organization: String? = null
    private var tosStatus: TosStatus? = null
    private var treatmentDataPresent = false
    private var onStartSession: (() -> Unit)? = null
    private var onClientUpdated: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentComponent = DaggerFragmentComponent.builder()
            .activityComponent((activity as BaseActivity?)?.activityComponent())
            .fragmentModule(FragmentModule(this))
            .build()
        fragmentComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogViewClientBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            tvName.text = "$firstName $lastName"
            tvDobValue.text = DateHelper.birthdayFormat(birthday)
            tvSexValue.text = if (isMale) getString(R.string.male) else getString(R.string.female)
            tvEmailValue.text = email

            if (username != null) {
                tvUsernameValue.text = username
            } else {
                tvUsernameValue.visibility = View.GONE
                tvUsername.visibility = View.GONE
            }
            tvOrganizationValue.text = organization

            when (tosStatus) {
                TosStatus.SIGNED, TosStatus.SIGNED_MANUALLY -> {
                    tvTosStatusSignedLabel.visibility = View.VISIBLE
                    tvTosStatusSignedIcon.visibility = View.VISIBLE
                }
                TosStatus.WAITING_SIGNATURE -> {
                    tvTosStatusWaitingLabel.visibility = View.VISIBLE
                    tvTosStatusWaitingIcon.visibility = View.VISIBLE
                }
                TosStatus.NOT_SIGNED -> {
                    tvTosStatusNotSignedLabel.visibility = View.VISIBLE
                    tvTosStatusNotSignedIcon.visibility = View.VISIBLE
                }
                else -> {
                    tvTosStatusNotSignedLabel.visibility = View.VISIBLE
                    tvTosStatusNotSignedIcon.visibility = View.VISIBLE
                }
            }

            tvEditClient.setOnClickListener { editClient() }
            tvViewHistory.setOnClickListener {
                sessionHistoryCommand.navigate(
                    requireActivity(),
                    clientId.toString(),
                    "$firstName $lastName",
                    treatmentDataPresent
                )
            }
            btnStartSession.isEnabled = treatmentDataPresent
            btnStartSession.setOnClickListener {
                dismiss()
                onStartSession?.invoke()
            }
        }
    }

    private fun editClient() {
        val editClientBottomSheet = EditClientBottomSheet.newInstance(
            onClientUpdated, clientId, firstName, lastName, birthday, isMale, email
        )
        editClientBottomSheet.show(parentFragmentManager, "EditClient BottomSheet")
//        dismiss()
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    companion object {
        fun newInstance(
            onStartSession: () -> Unit,
            onClientUpdated: () -> Unit,
            client: ClientUi,
            treatmentDataPresent: Boolean
        ) = ViewClientBottomSheet().apply {
            this.onStartSession = onStartSession
            this.onClientUpdated = onClientUpdated
            this.clientId = client.id
            this.firstName = client.firstName
            this.lastName = client.lastName
            this.birthday = client.birthday
            this.isMale = client.isMale
            this.email = client.email
            this.username = client.username
            this.organization = client.organization.name
            this.tosStatus = client.tosStatus
            this.treatmentDataPresent = treatmentDataPresent
        }
    }

}