package com.waveneuro.ui.dashboard.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import com.waveneuro.R
import com.waveneuro.databinding.ItemClientBinding
import com.waveneuro.ui.base.recycler.BaseEntityListAdapter
import com.waveneuro.ui.base.recycler.EntityVH
import com.waveneuro.ui.dashboard.home.adapter.model.PatientItem

class ClientListAdapter(
    context: Context,
    private val onItemClick: (PatientItem) -> Unit,
    private val onStartSessionClick: (PatientItem) -> Unit,
) : BaseEntityListAdapter<PatientItem, ClientListAdapter.PatientVH>(
    context,
    PatientItem.DiffCallback
){

    inner class PatientVH(
        private val binding: ItemClientBinding
    ) : EntityVH<PatientItem>(binding.root) {
        @SuppressLint("SetTextI18n")
        override fun bind(model: PatientItem) {
            super.bind(model)
            with(binding) {
                tvName.text = "${model.firstName} ${model.lastName}"
                tvOrganization.text = model.organization.name
                ivStartSession.setOnClickListener {
                    onStartSessionClick(model)
                }
                itemView.setOnClickListener {
                    onItemClick(model)
                }
                val startImage = if (model.isTosSigned) R.drawable.ic_start_session
                    else R.drawable.ic_start_session_disable
                ivStartSession.setImageResource(startImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientVH =
        PatientVH(ItemClientBinding.inflate(inflater, parent, false))

}