package com.waveneuro.ui.model.organization

import androidx.recyclerview.widget.DiffUtil

data class UiOrganization(
    val id: Int,
    val name: String,
) {

    object DiffCallback : DiffUtil.ItemCallback<UiOrganization>() {

        override fun areItemsTheSame(
            oldItem: UiOrganization,
            newItem: UiOrganization
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: UiOrganization,
            newItem: UiOrganization
        ): Boolean = oldItem == newItem

    }

}