package com.waveneuro.injection.component

import com.waveneuro.injection.module.MapperModule
import com.waveneuro.ui.model.client.mapper.ClientMapper
import com.waveneuro.ui.model.organization.mapper.OrganizationMapper
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MapperModule::class])
interface MapperComponent {

    fun organizationMapper(): OrganizationMapper

    fun clientMapper(): ClientMapper

}