package com.waveneuro.domain.model.device

import com.waveneuro.data.api.user.model.device.ApiSonalDevicesRs

interface DeviceMapper {
    fun fromApiToDomain(api: ApiSonalDevicesRs): SonalDevicesRs
}