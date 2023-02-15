package com.waveneuro.injection.module

import com.waveneuro.ui.model.client.mapper.ClientMapper
import com.waveneuro.ui.model.client.mapper.ClientMapperImpl
import com.waveneuro.ui.model.organization.mapper.OrganizationMapper
import com.waveneuro.ui.model.organization.mapper.OrganizationMapperImpl
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
    fun provideClientMapper(): ClientMapper {
        return ClientMapperImpl()
    }

}