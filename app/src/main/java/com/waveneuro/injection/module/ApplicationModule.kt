package com.waveneuro.injection.module

import android.app.Application
import android.content.Context
import com.asif.abase.injection.qualifier.ApplicationContext
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.waveneuro.data.analytics.AnalyticsManager
import com.waveneuro.data.analytics.AnalyticsManagerImpl
import com.waveneuro.data.api.common.buildBaseMoshi
import com.waveneuro.data.preference.PreferenceManager
import com.waveneuro.data.preference.PreferenceManagerImpl
import com.waveneuro.ui.base.handler.error.ErrorHandler
import com.waveneuro.ui.base.handler.error.ErrorHandlerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val myApplication: Application) {

    @Provides
    fun provideApplication(): Application {
        return myApplication
    }

    @Provides
    fun provideErrorHandler(): ErrorHandler {
        return ErrorHandlerImpl(buildBaseMoshi())
    }

    @Provides
    @ApplicationContext
    fun provideContext(): Context {
        return myApplication
    }

    @Singleton
    @Provides
    fun providePreferenceManager(@ApplicationContext context: Context): PreferenceManager {
        return PreferenceManagerImpl(context)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Singleton
    @Provides
    fun provideAnalyticsManager(@ApplicationContext context: Context): AnalyticsManager {
        return AnalyticsManagerImpl(context)
    }
}