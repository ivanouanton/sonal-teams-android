package com.waveneuro.ui.dashboard.home.mapper

import com.waveneuro.data.model.response.patient.PatientResponse
import com.waveneuro.ui.dashboard.home.adapter.model.PatientItem
import javax.inject.Inject

class PatientMapperImpl @Inject constructor() : PatientMapper {

    override fun fromDomainToUi(domain: PatientResponse) = with(domain) {
        PatientItem(
            id,
            firstName,
            lastName,
            birthday,
            email,
            username,
            isMale,
            organization,
            isTosSigned,
            tosStatus,
            alternativeIds,
            imageURLString,
        )
    }

}