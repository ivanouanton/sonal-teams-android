package com.waveneuro.injection.module

import com.waveneuro.ui.dashboard.home.mapper.PatientMapper
import com.waveneuro.ui.dashboard.home.mapper.PatientMapperImpl
import com.waveneuro.ui.dashboard.organization.mapper.OrganizationMapper
import com.waveneuro.ui.dashboard.organization.mapper.OrganizationMapperImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MapperModule {

    @Singleton
    @Provides
    fun provideOrganizationMapper(): OrganizationMapper {
        return OrganizationMapperImpl()
    }

    @Singleton
    @Provides
    fun providePatientMapper(): PatientMapper {
        return PatientMapperImpl()
    }

}