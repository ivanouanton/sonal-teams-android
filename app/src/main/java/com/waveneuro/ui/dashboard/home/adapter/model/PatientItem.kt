package com.waveneuro.ui.dashboard.home.adapter.model

import androidx.recyclerview.widget.DiffUtil
import com.waveneuro.data.model.response.common.TosStatus
import com.waveneuro.data.model.response.organization.OrganizationResponse
import com.waveneuro.data.model.response.patient.AlternativeId
import java.util.*

data class PatientItem(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val birthday: Date?,
    val email: String,
    val username: String?,
    val isMale: Boolean,
    val organization: OrganizationResponse,
    val isTosSigned: Boolean,
    val tosStatus: TosStatus?,
    val alternativeIds: List<AlternativeId>?,
    val imageURLString: String?,
) {

    object DiffCallback : DiffUtil.ItemCallback<PatientItem>() {

        override fun areItemsTheSame(
            oldItem: PatientItem,
            newItem: PatientItem
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: PatientItem,
            newItem: PatientItem
        ): Boolean = oldItem == newItem

    }

}
