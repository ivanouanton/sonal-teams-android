package com.waveneuro.domain.model.device

import com.waveneuro.data.api.user.model.device.ApiDevice
import com.waveneuro.data.api.user.model.device.ApiSonalDevicesRs
import javax.inject.Inject

class DeviceMapperImpl @Inject constructor() : DeviceMapper {

    override fun fromApiToDomain(api: ApiSonalDevicesRs): SonalDevicesRs = with(api) {
        SonalDevicesRs(
            devices.map(::fromApiToDomain)
        )
    }

    private fun fromApiToDomain(api: ApiDevice): Device = with(api) {
        Device(
            lastSessionAt,
            deviceName
        )
    }

}