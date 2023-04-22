package me.injent.myschool.core.network.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import me.injent.myschool.core.network.ApiProvider
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.retrofit.AuthTokenInterceptor
import me.injent.myschool.core.network.retrofit.RetrofitApiProvider
import me.injent.myschool.core.network.retrofit.RetrofitDnevnik
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun providesJsonConverterFactory() = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(
        interceptor: AuthTokenInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsNetworkModule {
        /**
         * Selects [RetrofitDnevnik] as default REST API tool
         */
        @Binds
        fun RetrofitDnevnik.bindsDnevnikNetworkDataSource(): DnevnikNetworkDataSource

        @Binds
        fun RetrofitApiProvider.bindsApiProvider(): ApiProvider
    }
}