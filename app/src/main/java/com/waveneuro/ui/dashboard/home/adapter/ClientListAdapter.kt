package com.waveneuro.ui.dashboard.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.waveneuro.databinding.ItemClientBinding
import com.waveneuro.ui.base.recycler.BaseEntityListAdapter
import com.waveneuro.ui.base.recycler.EntityVH
import com.waveneuro.ui.model.client.ClientUi

class ClientListAdapter(
    context: Context,
    private val onItemClick: (ClientUi) -> Unit,
    private val onMoreRequested: () -> Unit,
) : BaseEntityListAdapter<ClientUi, ClientListAdapter.ClientVH>(
    context,
    ClientUi.DiffCallback
){

    var totalCurrentCount: Int = 0

    override fun getItemCount(): Int {
        val itemCount = super.getItemCount()
        totalCurrentCount = itemCount

        return itemCount
    }

    inner class ClientVH(
        private val binding: ItemClientBinding
    ) : EntityVH<ClientUi>(binding.root) {
        @SuppressLint("SetTextI18n")
        override fun bind(model: ClientUi) {
            super.bind(model)
            val initials = "${model.firstName[0]}${model.lastName[0]}".trim().uppercase()

            with(binding) {
                tvName.text = "${model.firstName} ${model.lastName}"
                tvOrganization.text = model.organization.name
                tvInitials.text = initials
                tvInitials.isVisible = model.imageURLString == null
                itemView.setOnClickListener { onItemClick(model) }
                Glide.with(root.context)
                    .load(model.imageURLString)
                    .into(ivProfileImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientVH =
        ClientVH(ItemClientBinding.inflate(inflater, parent, false))

    override fun onBindViewHolder(holder: ClientVH, position: Int) {
        super.onBindViewHolder(holder, position)

        if (position == totalCurrentCount - 1) {
            onMoreRequested()
        }
    }

}