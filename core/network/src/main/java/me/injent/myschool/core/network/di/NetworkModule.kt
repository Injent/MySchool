package me.injent.myschool.core.network.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import me.injent.myschool.core.network.DnevnikNetworkDataSource
import me.injent.myschool.core.network.retrofit.AuthInterceptor
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
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface OptionalNetworkModule {
    /**
     * Selecting [RetrofitDnevnik] as default REST API tool
     */
    @Binds
    fun RetrofitDnevnik.binds(): DnevnikNetworkDataSource
}