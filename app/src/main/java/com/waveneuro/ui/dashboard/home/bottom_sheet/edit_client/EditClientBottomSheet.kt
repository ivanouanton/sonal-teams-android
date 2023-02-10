package com.waveneuro.ui.dashboard.home.bottom_sheet.edit_client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.waveneuro.R
import com.waveneuro.data.model.request.common.SexType
import com.waveneuro.databinding.DialogEditClientBinding
import com.waveneuro.injection.component.DaggerFragmentComponent
import com.waveneuro.injection.component.FragmentComponent
import com.waveneuro.injection.module.FragmentModule
import com.waveneuro.ui.base.BaseActivity
import com.waveneuro.ui.dashboard.home.bottom_sheet.edit_client.EditClientViewState.Error
import com.waveneuro.ui.dashboard.home.bottom_sheet.edit_client.EditClientViewState.Success
import com.waveneuro.utils.DateHelper
import com.waveneuro.utils.ext.toast
import java.util.*
import javax.inject.Inject

class EditClientBottomSheet : BottomSheetDialogFragment() {

    @Inject
    lateinit var editClientViewModel: EditClientViewModel

    private lateinit var fragmentComponent: FragmentComponent
    private lateinit var binding: DialogEditClientBinding

    private var clientId = 0
    private var firstName: String? = null
    private var lastName: String? = null
    private var birthday: String? = null
    private var isMale: Boolean = false
    private var email: String? = null
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
        binding = DialogEditClientBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            tipFirstName.setText(firstName ?: "")
            tipLastName.setText(lastName ?: "")
            tipBirthday.setText(DateHelper.birthdayFormat(birthday))
            tipEmail.setText(email ?: "")

            rbMale.isChecked = isMale
            rbFemale.isChecked = !isMale

            btnSaveChanges.setOnClickListener { onClickSave() }
            tipBirthday.setOnClickListener { showDatePicker() }

            editClientViewModel.dataEditClientLive.observe(requireActivity(), viewStateObserver)
        }
    }

    private var viewStateObserver = Observer<EditClientViewState> { viewState ->
        when (viewState) {
            is Success -> {
                dismiss()
                onClientUpdated?.invoke()
            }
            is Error -> viewState.message?.let { requireActivity().toast(it) }
        }
    }

    private fun showDatePicker() {
        MaterialDatePicker.Builder.datePicker()
            .setSelection(DateHelper.getBirthdayDate(birthday)?.time)
            .setTitleText(R.string.date_picker_birth_title)
            .build().apply {
                addOnPositiveButtonClickListener {
                    birthday = DateHelper.birthdayFormat(Date(it))
                    binding.tipBirthday.setText(birthday ?: "")
                }
            }
            .show(childFragmentManager, "DatePicker")
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    private fun onClickSave() {
        with(binding) {
            editClientViewModel.updateClient(
                clientId,
                tipFirstName.text?.trim().toString(),
                tipLastName.text?.trim().toString(),
                tipBirthday.text?.trim().toString(),
                tipEmail.text?.trim().toString(),
                if (rbMale.isChecked) SexType.MALE else SexType.FEMALE
            )
        }
    }

    companion object {
        fun newInstance(
            onClientUpdated: (() -> Unit)?,
            id: Int,
            name: String?,
            lastName: String?,
            birthday: String?,
            sex: Boolean,
            email: String?
        ) = EditClientBottomSheet().apply {
            this.clientId = id
            this.firstName = name
            this.lastName = lastName
            this.birthday = birthday
            this.isMale = sex
            this.email = email
            this.onClientUpdated = onClientUpdated
        }
    }

}