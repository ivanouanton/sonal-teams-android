package com.waveneuro.domain.usecase.user

import com.waveneuro.data.api.user.UserApi
import com.waveneuro.domain.model.device.DeviceMapperImpl
import com.waveneuro.domain.model.device.SonalDevicesRs
import javax.inject.Inject

class GetSonalDevicesUseCase @Inject constructor(
    private val serviceApi: UserApi,
    private val mapper: DeviceMapperImpl
) {

    suspend fun getSonalDevices(): SonalDevicesRs =
        mapper.fromApiToDomain(serviceApi.getSonalDevices())

}