package com.waveneuro.injection.module

import com.waveneuro.BuildConfig
import com.waveneuro.data.Config
import com.waveneuro.data.api.common.HeaderInterceptor
import com.waveneuro.data.api.common.buildBaseMoshi
import com.waveneuro.data.api.session.SessionApi
import com.waveneuro.data.api.user.UserApi
import com.waveneuro.data.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideUserApi(okHttpClient: OkHttpClient): UserApi =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Config.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(buildBaseMoshi()))
            .build()
            .create(UserApi::class.java)

    @Singleton
    @Provides
    fun provideSessionApi(okHttpClient: OkHttpClient): SessionApi =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Config.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(buildBaseMoshi()))
            .build()
            .create(SessionApi::class.java)

    @Singleton
    @Provides
    fun buildHttpClientWithAuthenticator(
        headerInterceptor: Interceptor,
    ) = with(OkHttpClient.Builder()) {
        interceptors().apply {
            add(headerInterceptor)
            add(HttpLoggingInterceptor {
                if (it.contains("�")) {
                    Timber.w("--- skip binary logging ---")
                } else {
                    Timber.i(it)
                }
            }.apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else level
            })
        }
        protocols(listOf(Protocol.HTTP_1_1))
        build()
    }

    @Singleton
    @Provides
    fun provideHeaderInterceptor(prefManager: PreferenceManager): Interceptor =
        HeaderInterceptor(prefManager)

}