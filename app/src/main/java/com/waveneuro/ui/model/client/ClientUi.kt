package com.waveneuro.ui.model.client

import androidx.recyclerview.widget.DiffUtil
import com.waveneuro.data.model.response.client.AlternativeId
import com.waveneuro.data.model.response.common.TosStatus
import com.waveneuro.data.model.response.organization.OrganizationResponse

data class ClientUi(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val birthday: String?,
    val email: String,
    val username: String?,
    val isMale: Boolean,
    val organization: OrganizationResponse,
    val isTosSigned: Boolean,
    val tosStatus: TosStatus?,
    val alternativeIds: List<AlternativeId>?,
    val imageURLString: String?,
) {

    object DiffCallback : DiffUtil.ItemCallback<ClientUi>() {

        override fun areItemsTheSame(
            oldItem: ClientUi,
            newItem: ClientUi
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ClientUi,
            newItem: ClientUi
        ): Boolean = oldItem == newItem

    }

}
