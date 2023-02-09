package com.waveneuro.injection.component

import com.waveneuro.injection.module.MapperModule
import com.waveneuro.ui.dashboard.home.mapper.PatientMapper
import com.waveneuro.ui.dashboard.organization.mapper.OrganizationMapper
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MapperModule::class])
interface MapperComponent {

    fun organizationMapper(): OrganizationMapper

    fun patientMapper(): PatientMapper

}