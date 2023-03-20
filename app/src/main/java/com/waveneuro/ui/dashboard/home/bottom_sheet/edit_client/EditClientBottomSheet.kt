package com.waveneuro.ui.dashboard.home.bottom_sheet.edit_client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.waveneuro.R
import com.waveneuro.domain.model.common.SexType
import com.waveneuro.databinding.DialogEditClientBinding
import com.waveneuro.ui.base.utils.EventObserver
import com.waveneuro.ui.dashboard.home.bottom_sheet.edit_client.EditClientViewState.Success
import com.waveneuro.utils.DateHelper
import com.waveneuro.utils.ext.toast
import java.util.*

class EditClientBottomSheet : BottomSheetDialogFragment() {

    private val viewModel: EditClientViewModel by viewModels()

    private lateinit var binding: DialogEditClientBinding

    private var clientId = 0
    private var firstName: String? = null
    private var lastName: String? = null
    private var birthday: String? = null
    private var isMale: Boolean = false
    private var onClientUpdated: ((String?) -> Unit)? = null

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

            rbMale.isChecked = isMale
            rbFemale.isChecked = !isMale

            btnSaveChanges.setOnClickListener { onClickSave() }
            tipBirthday.setOnClickListener { showDatePicker() }
        }

        setObserver()
    }

    private fun setObserver() {
        viewModel.dataEditClientLive.observe(requireActivity(), Observer { viewState ->
            when (viewState) {
                is Success -> {
                    onClientUpdated?.invoke(viewState.fullName)
                    dismiss()
                }
            }
        })
        viewModel.message.observe(viewLifecycleOwner, EventObserver(::showMessage))
    }

    private fun showMessage(message: String?) {
        requireActivity().toast(message)
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
            viewModel.updateClient(
                clientId,
                tipFirstName.text?.trim().toString(),
                tipLastName.text?.trim().toString(),
                tipBirthday.text?.trim().toString(),
                if (rbMale.isChecked) SexType.MALE else SexType.FEMALE
            )
        }
    }

    companion object {
        fun newInstance(
            onClientUpdated: ((String?) -> Unit)?,
            id: Int,
            name: String?,
            lastName: String?,
            birthday: String?,
            sex: Boolean,
        ) = EditClientBottomSheet().apply {
            this.clientId = id
            this.firstName = name
            this.lastName = lastName
            this.birthday = birthday
            this.isMale = sex
            this.onClientUpdated = onClientUpdated
        }
    }

}