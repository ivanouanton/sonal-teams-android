package com.waveneuro.ui.dashboard.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.waveneuro.R
import com.waveneuro.data.model.response.organization.OrganizationResponse
import com.waveneuro.databinding.DialogFiltersBinding
import com.waveneuro.injection.component.DaggerFragmentComponent
import com.waveneuro.injection.component.FragmentComponent
import com.waveneuro.injection.module.FragmentModule
import com.waveneuro.ui.base.BaseActivity

class FiltersBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: DialogFiltersBinding
    private lateinit var adapter: OrgsListAdapter
    private lateinit var fragmentComponent: FragmentComponent

    private var organizationList: List<OrganizationResponse> = listOf()
    private var selectedIds: MutableList<Int> = mutableListOf()
    private var onFiltersChanged: ((List<Int>) -> Unit)? = null

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
        binding = DialogFiltersBinding.inflate(layoutInflater)

        adapter = OrgsListAdapter(requireContext(), organizationList, selectedIds, ::onSelected, ::onDeselected)
        binding.rvOrganizations.adapter = adapter

        binding.tvClear.setOnClickListener {
            selectedIds.clear()
            adapter.notifyDataSetChanged()
        }
        binding.btnApply.setOnClickListener {
            onFiltersChanged?.invoke(selectedIds)
            dismiss()
        }

        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialog
    }

    private fun onSelected(id: Int) {
        selectedIds.add(id)
    }

    private fun onDeselected(id: Int) {
        selectedIds.remove(id)
    }

    companion object {
        fun newInstance(
            organizations: List<OrganizationResponse>,
            selected: List<Int>,
            onApplyClick: (List<Int>) -> Unit
        ): FiltersBottomSheet = FiltersBottomSheet().apply {
            this.organizationList = organizations
            this.selectedIds = selected.toMutableList()
            this.onFiltersChanged = onApplyClick
        }
    }
}